import axios from 'axios';
import type { UserRegisterDTO, UserLoginDTO, LoginResponseDTO } from '../types/User';
import type {Profile} from "../types/Profile.ts";

const API_URL = 'http://localhost:8080/api/user';

export const registerUser = (data: UserRegisterDTO) => axios.post(`${API_URL}/save`, data);
// 로그인하면서 토큰 자동 저장
export const loginUser = async (data: UserLoginDTO): Promise<LoginResponseDTO> => {
    const { data: loginData }: { data: LoginResponseDTO } = await axios.post(`${API_URL}/login`, data);
    localStorage.setItem('token', loginData.token);
    localStorage.setItem('userId', loginData.userId);
    return loginData;
};

export async function getProfile(token: string): Promise<Profile> {
    const response = await axios.get('/api/user/profile', {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });
    return response.data;
}

export async function uploadProfileImage(formData: FormData, token: string): Promise<void> {
    await axios.post('/api/user/uploadProfileImage', formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${token}`
        }
    });
}