package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.repository.DeclarationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DeclarationService {

    private final DeclarationRepository declarationRepository;

    public void deleteStudentDeclarationById(Long studentId) {
        declarationRepository.deleteDeclarationByStudentId(studentId);
    }
}
