import apiClient from './api';

export async function fetchStudentsWithDeclaration(courseId, page = 0, size = 10) {
    const res = await apiClient.get(`/courses/${courseId}/students/with-declarations`, {
        params: { page, size }
    });
    return res.data;
}

export async function fetchInactiveStudents(courseId, page = 0, size = 10) {
    const res = await apiClient.get(`/courses/${courseId}/students/inactive`, {
        params: { page, size }
    });
    return res.data;
}

export async function fetchStudentsWithoutDeclaration(courseId, page = 0, size = 10) {
    const res = await apiClient.get(`/courses/${courseId}/students/no-declarations`, {
        params: { page, size }
    });
    return res.data;
}

export async function fetchAllStudentsData(courseId, page = 0, size = 10) {
    const [withDecl, inactive, withoutDecl] = await Promise.all([
        fetchStudentsWithDeclaration(courseId, page, size),
        fetchInactiveStudents(courseId, page, size),
        fetchStudentsWithoutDeclaration(courseId, page, size),
    ]);

    return { withDecl, inactive, withoutDecl };
}

export async function markStudentInactive(courseId, studentId) {
    const res = await apiClient.post(`/courses/${courseId}/students/${studentId}/inactive`);
    return res.data;
}

export async function markStudentActive(courseId, studentId) {
    await apiClient.delete(`/courses/${courseId}/students/${studentId}/active`);
    return true;
}
