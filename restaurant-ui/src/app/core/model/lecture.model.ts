
import { Course } from "./course.model";

export interface Lecture{
    id?: number;
    title: string;
    description: string;
    order: number;
    url: string;
    course: Course;
    sectionId: number;
}
