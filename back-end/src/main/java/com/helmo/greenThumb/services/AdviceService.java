package com.helmo.greenThumb.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.UserRecord;
import com.helmo.greenThumb.dto.AdviceDTO;
import com.helmo.greenThumb.infrastructures.AdviceRepository;
import com.helmo.greenThumb.model.Advice;
import com.helmo.greenThumb.utils.DTOConverter;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdviceService {
    private static int PAGE_SIZE = 5;
    
    @Autowired
    private AdviceRepository adviceRepository;
    
    @Autowired
    private FirebaseService firebaseService;

    Map<String, UserRecord> userCache = new HashMap<>();

    private static final DTOConverter DTO_CONVERTER = new DTOConverter();
    public Advice createAdvice(Advice advice) {
        return adviceRepository.save(advice);
    }

    public List<AdviceDTO> getAdviceByPage(String uid, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Advice> advicesPage = adviceRepository.findAllByOrderByDateDesc(pageable);
        return advicesPage.stream()
                .map(advice -> {
                    UserRecord userRecord;
                    String authorUid = advice.getAuthor().getUid();
                    if (!userCache.containsKey(authorUid)) {
                        userRecord = firebaseService.getUserByUid(authorUid);
                        userCache.put(authorUid, userRecord);
                    }else{
                        userRecord = userCache.get(authorUid);
                    }
                    return DTO_CONVERTER.toAdviceDTO(advice, userRecord, uid);
                })
                .collect(Collectors.toList());
    }

    public Advice getAdviceById(Long id) {
        return adviceRepository.findById(id).orElse(null);
    }

    public void deleteAdvice(Long id) {
        adviceRepository.deleteById(id);
    }
}
