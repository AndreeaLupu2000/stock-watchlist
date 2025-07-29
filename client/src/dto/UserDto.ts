export interface UserDto {
    id: number;
    username: string;
    password: string;
}

export interface RegisterDto {
    username: string;
    password: string;
}

export interface LoginDto {
    username: string;
    password: string;
}