export interface AnswerLogin{
    refreshToken: string;
    user_id: number;
    username: string;
    email: string;
    roles: string[];
    token: string;
    tokenType: string;
}