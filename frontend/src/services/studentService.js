export async function fetchStudentsWithDeclaration() {
    const res = await fetch("/api/students/with-declarations");
    if (!res.ok) throw new Error("Failed to fetch students with declarations");
    return res.json();
}

export async function fetchInactiveStudents() {
    const res = await fetch("/api/students/inactive");
    if (!res.ok) throw new Error("Failed to fetch inactive students");
    return res.json();
}

export async function fetchStudentsWithoutDeclaration() {
    const res = await fetch("/api/students/no-declarations");
    if (!res.ok) throw new Error("Failed to fetch students without declarations");
    return res.json();
}

export async function fetchAllStudentsData() {
    const [withDecl, inactive, withoutDecl] = await Promise.all([
        fetchStudentsWithDeclaration(),
        fetchInactiveStudents(),
        fetchStudentsWithoutDeclaration(),
    ]);

    return { withDecl, inactive, withoutDecl };
}
