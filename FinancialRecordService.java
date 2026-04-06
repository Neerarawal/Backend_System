package com.zorvyn.finance.service;

import com.zorvyn.finance.dto.request.FinancialRecordRequest;
import com.zorvyn.finance.dto.response.FinancialRecordResponse;
import com.zorvyn.finance.entity.FinancialRecord;
import com.zorvyn.finance.entity.User;
import com.zorvyn.finance.enums.RecordType;
import com.zorvyn.finance.exception.BadRequestException;
import com.zorvyn.finance.exception.ResourceNotFoundException;
import com.zorvyn.finance.repository.FinancialRecordRepository;
import com.zorvyn.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    public Page<FinancialRecordResponse> getRecords(RecordType type,
                                                     String category,
                                                     LocalDate from,
                                                     LocalDate to,
                                                     int page,
                                                     int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        Page<FinancialRecord> records;

        boolean hasType = type != null;
        boolean hasCategory = category != null && !category.isBlank();
        boolean hasDateRange = from != null && to != null;

        if (hasType && hasCategory) {
            records = recordRepository.findAllByTypeAndCategoryIgnoreCaseAndDeletedFalse(type, category, pageable);
        } else if (hasType) {
            records = recordRepository.findAllByTypeAndDeletedFalse(type, pageable);
        } else if (hasCategory) {
            records = recordRepository.findAllByCategoryIgnoreCaseAndDeletedFalse(category, pageable);
        } else if (hasDateRange) {
            if (from.isAfter(to)) {
                throw new BadRequestException("'from' date cannot be after 'to' date");
            }
            records = recordRepository.findAllByDateBetweenAndDeletedFalse(from, to, pageable);
        } else {
            records = recordRepository.findAllByDeletedFalse(pageable);
        }

        return records.map(this::toResponse);
    }

    public FinancialRecordResponse getById(Long id) {
        return toResponse(findRecord(id));
    }

    public FinancialRecordResponse create(FinancialRecordRequest request) {
        User currentUser = resolveCurrentUser();

        FinancialRecord record = FinancialRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory().trim())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(currentUser)
                .deleted(false)
                .build();

        return toResponse(recordRepository.save(record));
    }

    public FinancialRecordResponse update(Long id, FinancialRecordRequest request) {
        FinancialRecord record = findRecord(id);

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory().trim());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());

        return toResponse(recordRepository.save(record));
    }

    public void delete(Long id) {
        FinancialRecord record = findRecord(id);
        record.setDeleted(true);
        recordRepository.save(record);
    }

    private FinancialRecord findRecord(Long id) {
        return recordRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial record not found with id: " + id));
    }

    private User resolveCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private FinancialRecordResponse toResponse(FinancialRecord r) {
        return FinancialRecordResponse.builder()
                .id(r.getId())
                .amount(r.getAmount())
                .type(r.getType())
                .category(r.getCategory())
                .date(r.getDate())
                .notes(r.getNotes())
                .createdBy(r.getCreatedBy().getUsername())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
