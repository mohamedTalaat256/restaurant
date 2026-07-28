import { Lecture } from "./lecture.model";

export interface LectureSection {
    id?: number;
    title: string;
    courseId: number;
    lectures?: Lecture[];
}
