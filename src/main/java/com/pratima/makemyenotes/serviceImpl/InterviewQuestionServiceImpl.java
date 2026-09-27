package com.pratima.makemyenotes.serviceImpl;
import com.pratima.makemyenotes.dto.InterviewQuestionDTO;
import com.pratima.makemyenotes.entity.InterviewQuestion;
import com.pratima.makemyenotes.entity.Question;
import com.pratima.makemyenotes.exceptions.NotFoundException;
import com.pratima.makemyenotes.repository.InterviewQuestionRepository;
import com.pratima.makemyenotes.repository.QuestionRepository;
import com.pratima.makemyenotes.service.InterviewQuestionService;
import com.pratima.makemyenotes.utility.ConverterUtility;
import com.pratima.makemyenotes.utility.DateTimeUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewQuestionServiceImpl implements InterviewQuestionService {

    @Autowired
    private InterviewQuestionRepository interviewQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private DateTimeUtility dateTimeUtility;

    @Autowired
    private ConverterUtility converterUtility;

    // ─── Add ──────────────────────────────────────────────

    @Override
    @Transactional
    public InterviewQuestionDTO save(InterviewQuestionDTO dto) {
        Question parent = questionRepository.findById(dto.getParentQuestionId())
                .orElseThrow(() -> new NotFoundException(
                        "Parent question not found with id: " + dto.getParentQuestionId()));
        InterviewQuestion entity = converterUtility.interviewQuestionDTOToInterviewQuestion(dto);
        entity.setParentQuestion(parent);
        InterviewQuestion savedEntity = interviewQuestionRepository.save(entity);
        return converterUtility.interviewQuestionToInterviewQuestionDTO(savedEntity);
    }

    // ─── Get All by Parent ────────────────────────────────
    @Override
    public List<InterviewQuestionDTO> getByParentQuestionId(Long parentQuestionId) {
        Question parent = questionRepository.findById(parentQuestionId)
                .orElseThrow(() -> new NotFoundException(
                        "Parent question not found with id: " + parentQuestionId));

        List<InterviewQuestion> interviewQuestionList = interviewQuestionRepository
                .findByParentQuestionOrderByCreatedDateAsc(parent);

        List<InterviewQuestionDTO> dtoList = interviewQuestionList.stream()
                .map(converterUtility::interviewQuestionToInterviewQuestionDTO)
                .collect(Collectors.toList());

        return dtoList;
    }

    // ─── Get Single ───────────────────────────────────────

    @Override
    public InterviewQuestionDTO getById(Long id) {
        return toDTO(findIQOrThrow(id));
    }

    // ─── Update ───────────────────────────────────────────

    @Override
    @Transactional
    public InterviewQuestionDTO update(Long id, InterviewQuestionDTO dto) {
        InterviewQuestion existing = interviewQuestionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Interview question not found with id: " + id));

        if(dto.getQuestion()!=null){
            existing.setQuestion(dto.getQuestion());
        }
        if(dto.getAnswer()!=null){
            existing.setAnswer(dto.getAnswer());
        }
        if(dto.getExpanded()){
            existing.setExpanded(dto.getExpanded());
        }else{
            existing.setExpanded(dto.getExpanded());
        }
        InterviewQuestion result = interviewQuestionRepository.save(existing);
        return converterUtility.interviewQuestionToInterviewQuestionDTO(result);
    }

    // ─── Delete Single ────────────────────────────────────

    @Override
    @Transactional
    public void delete(Long id) {
        InterviewQuestion iq = findIQOrThrow(id);
        interviewQuestionRepository.delete(iq);
    }

    // ─── Delete All by Parent ─────────────────────────────

    @Override
    @Transactional
    public void deleteAllByParentQuestionId(Long parentQuestionId) {
        Question parent = findParentOrThrow(parentQuestionId);
        interviewQuestionRepository.deleteByParentQuestion(parent);
    }

    // ─── Private Helpers ──────────────────────────────────

    private Question findParentOrThrow(Long parentQuestionId) {
        return questionRepository.findById(parentQuestionId)
                .orElseThrow(() -> new NotFoundException(
                        "Parent question not found with id: " + parentQuestionId));
    }

    private InterviewQuestion findIQOrThrow(Long id) {
        return interviewQuestionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Interview question not found with id: " + id));
    }

    // ─── Mappers ──────────────────────────────────────────

    private InterviewQuestionDTO toDTO(InterviewQuestion entity) {
        return InterviewQuestionDTO.builder()
                .id(entity.getId())
                .question(entity.getQuestion())
                .answer(entity.getAnswer())
                .addedBy(entity.getAddedBy())
                .createdDate(dateTimeUtility.convertDateToLocalDateTime(entity.getCreatedDate()))
                .modifiedDate(dateTimeUtility.convertDateToLocalDateTime(entity.getModifiedDate()))
                .parentQuestionId(entity.getParentQuestion().getId())
                .parentQuestionTitle(entity.getParentQuestion().getQuestion())
                .build();
    }
}
