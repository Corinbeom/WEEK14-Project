import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import PostBoard from "./pages/Board/PostBoard.tsx";
import GetBoards from "./pages/Board/GetBoards.tsx";
import BoardDetail from "./pages/Board/BoardDetail.tsx";
import EditBoard from "./pages/Board/EditBoard.tsx";
import ProfilePage from "./pages/ProfilePage.tsx";

export default function Router() {
    return (
        <BrowserRouter basename="/">
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/boards" element={<GetBoards />} />
                <Route path="/boards/new" element={<PostBoard />} />
                <Route path="/board/:id" element={<BoardDetail />} />
                <Route path="/board/:id/edit" element={<EditBoard />} />
                <Route path="/profile" element={<ProfilePage />} />
                <Route path="/boards/:type" element={<GetBoards />} />
            </Routes>
        </BrowserRouter>
);
}
