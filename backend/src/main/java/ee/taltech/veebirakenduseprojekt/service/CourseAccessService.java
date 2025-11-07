package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CourseAccessService {

    private final TeacherRepository teacherRepository;

    public void requireTeacherAccess(String email, Long courseId) {
        boolean isTeacher = teacherRepository.isTeacherOfCourse(email, courseId);
        if (!isTeacher) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a teacher of this course");
        }
    }
}
