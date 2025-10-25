import apiClient from './api';

export async function fetchStudentsWithDeclaration(courseId) {
    const res = await apiClient.get(`/courses/${courseId}/students/with-declarations`);
    return res.data;
}

export async function fetchInactiveStudents(courseId) {
    const res = await apiClient.get(`/courses/${courseId}/students/inactive`);
    return res.data;
}

export async function fetchStudentsWithoutDeclaration(courseId) {
    const res = await apiClient.get(`/courses/${courseId}/students/no-declarations`);
    return res.data;
}

export async function fetchAllStudentsData(courseId) {
    const [withDecl, inactive, withoutDecl] = await Promise.all([
        fetchStudentsWithDeclaration(courseId),
        fetchInactiveStudents(courseId),
        fetchStudentsWithoutDeclaration(courseId),
    ]);

    return { withDecl, inactive, withoutDecl };
}
