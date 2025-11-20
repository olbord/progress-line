import { getToken } from "../msal/msalInstance.js"
import apiClient from "@/services/api.js";

export async function fetchStudentsWithDeclaration(courseId, page = 0, size = 10) {
    const token = await getToken();
    const res = await apiClient.get(`/courses/${courseId}/students/with-declarations`, {
        headers: { Authorization: `Bearer ${token}` },
        params: { page, size }
    });
    return res.data;
}

export async function fetchInactiveStudents(courseId, page = 0, size = 10) {
    const token = await getToken();
    const res = await apiClient.get(`/courses/${courseId}/students/inactive`, {
        headers: { Authorization: `Bearer ${token}` },
        params: { page, size }
    });
    return res.data;
}

export async function fetchStudentsWithoutDeclaration(courseId, page = 0, size = 10) {
    const token = await getToken();
    const res = await apiClient.get(`/courses/${courseId}/students/no-declarations`, {
        headers: { Authorization: `Bearer ${token}` },
        params: { page, size }
    });
    return res.data;
}

export async function fetchAllStudentsData(courseId, page = 0, size = 10) {
    return {
        withDecl: await fetchStudentsWithDeclaration(courseId, page, size),
        inactive: await fetchInactiveStudents(courseId, page, size),
        withoutDecl: await fetchStudentsWithoutDeclaration(courseId, page, size)
    };
}

export async function markStudentInactive(courseId, studentId) {
    const token = await getToken();
    const res = await apiClient.post(
        `/courses/${courseId}/students/${studentId}/inactive`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
    );
    return res.data;
}

export async function markStudentActive(courseId, studentId) {
    const token = await getToken();
    const res = await apiClient.delete(
        `/courses/${courseId}/students/${studentId}/active`,
        { headers: { Authorization: `Bearer ${token}` } }
    );
    return res.data;
}
