export interface UserRegisterDTO {
    userName: string;
    userId: string;
    userEmail: string;
    password: string;
    userBirth: string;
}

export interface UserLoginDTO {
    userId: string;
    password: string;
}

export interface LoginResponseDTO {
    token: string;
    userName: string;
    userEmail: string;
    userId: string;
}
