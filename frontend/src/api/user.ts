import axios from 'axios';
import type { UserRegisterDTO, UserLoginDTO, LoginResponseDTO } from '../types/User';

const API_URL = 'http://localhost:8080/api/user';

export const registerUser = (data: UserRegisterDTO) => axios.post(`${API_URL}/save`, data);
export const loginUser = (data: UserLoginDTO) => axios.post<LoginResponseDTO>(`${API_URL}/login`, data);
