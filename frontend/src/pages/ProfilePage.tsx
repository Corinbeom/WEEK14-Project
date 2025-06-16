import { useEffect, useRef, useState } from 'react';
import type { Profile } from '../types/Profile';
import { getProfile, uploadProfileImage } from '../api/user';
import { useNavigate } from 'react-router-dom';
import { AnimatePresence, motion } from 'framer-motion';
import './ProfilePage.css';

export default function ProfilePage() {
    const [profile, setProfile] = useState<Profile | null>(null);
    const token = localStorage.getItem('token') || '';
    const fileInputRef = useRef<HTMLInputElement>(null);
    const navigate = useNavigate();

    const [postPage, setPostPage] = useState(0);
    const [commentPage, setCommentPage] = useState(0);
    const [likePage, setLikePage] = useState(0);
    const [direction, setDirection] = useState(0);

    const pageSize = 4;

    useEffect(() => {
        if (!token) {
            alert('로그인 후 이용해 주세요.');
            return;
        }
        const fetchProfile = async () => {
            try {
                const data = await getProfile(token);
                setProfile(data);
            } catch (err) {
                alert('프로필 조회 실패');
            }
        };
        fetchProfile();
    }, [token]);

    const handleImageClick = () => fileInputRef.current?.click();

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0 && profile) {
            const file = e.target.files[0];
            const formData = new FormData();
            formData.append("file", file);

            try {
                await uploadProfileImage(formData, token);
                alert("프로필 이미지 변경 완료");
                navigate('/profile');
            } catch {
                alert("업로드 실패");
            }
        }
    };

    const paginate = (items: any[], page: number) =>
        items.slice(page * pageSize, (page + 1) * pageSize);

    const variants = {
        enter: (dir: number) => ({ x: dir > 0 ? 300 : -300, opacity: 0 }),
        center: { x: 0, opacity: 1 },
        exit: (dir: number) => ({ x: dir > 0 ? -300 : 300, opacity: 0 })
    };

    if (!profile) return <div>Loading...</div>;

    // 🔥 공통으로 버튼 렌더링 함수 만들어서 반복 줄이기
    const ArrowButton = ({ direction, onClick }: { direction: 'left' | 'right', onClick: () => void }) => (
        <button className={`slider-button ${direction}`} onClick={onClick}>
            <img src={`/${direction}-arrow.png`} alt={`${direction} arrow`} style={{ width: 24, height: 24 }} />
        </button>
    );

    return (
        <div className="profile-page">
            <div className="profile-info">
                <div className="profile-image-wrapper" onClick={handleImageClick}>
                    <img src={profile.profileImage} alt="프로필 이미지" className="profile-img" />
                </div>

                <input type="file" style={{ display: 'none' }} ref={fileInputRef} onChange={handleFileChange} />
                <div className="profile-name">{profile.userName}</div>
                <div className="profile-email">{profile.userEmail}</div>
            </div>

            <div className="profile-activity">
                <h2>내 활동</h2>

                {/* 내가 작성한 글 */}
                <div className="activity-section">
                    <h3>내가 작성한 글</h3>
                    <div className="activity-grid-wrapper">
                        <ArrowButton direction="left" onClick={() => { setDirection(-1); setPostPage(Math.max(postPage - 1, 0)) }} />
                        <div className="activity-grid">
                            <AnimatePresence custom={direction} mode="popLayout">
                                <motion.div
                                    key={postPage}
                                    custom={direction}
                                    variants={variants}
                                    initial="enter"
                                    animate="center"
                                    exit="exit"
                                    transition={{ duration: 0.5 }}
                                    className="activity-slider"
                                    style={{ display: 'flex', gap: '1rem' }}
                                ><>
                                    {paginate(profile.myPosts, postPage).map(post => (
                                        <div key={post.id} className="activity-card" onClick={() => navigate(`/board/${post.id}`)}>
                                            {post.title}
                                        </div>
                                    ))}
                                </>
                                </motion.div>
                            </AnimatePresence>
                        </div>
                        <ArrowButton direction="right" onClick={() => { setDirection(1); setPostPage(postPage + 1) }} />
                    </div>
                </div>

                {/* 내가 작성한 댓글 */}
                <div className="activity-section">
                    <h3>내가 작성한 댓글</h3>
                    <div className="activity-grid-wrapper">
                        <ArrowButton direction="left" onClick={() => { setDirection(-1); setCommentPage(Math.max(commentPage - 1, 0)) }} />
                        <div className="activity-grid">
                            <AnimatePresence custom={direction} mode="popLayout">
                                <motion.div
                                    key={commentPage}
                                    custom={direction}
                                    variants={variants}
                                    initial="enter"
                                    animate="center"
                                    exit="exit"
                                    transition={{ duration: 0.5 }}
                                    className="activity-slider"
                                    style={{ display: 'flex', gap: '1rem' }}
                                ><>
                                    {paginate(profile.myComments, commentPage).map(comment => (
                                        <div key={comment.id} className="activity-card" onClick={() => navigate(`/board/${comment.postId}`)}>
                                            {comment.content}
                                        </div>
                                    ))}
                                </>
                                </motion.div>
                            </AnimatePresence>
                        </div>
                        <ArrowButton direction="right" onClick={() => { setDirection(1); setCommentPage(commentPage + 1) }} />
                    </div>
                </div>

                {/* 내가 좋아요한 글 */}
                <div className="activity-section">
                    <h3>내가 좋아요한 글</h3>
                    <div className="activity-grid-wrapper">
                        <ArrowButton direction="left" onClick={() => { setDirection(-1); setLikePage(Math.max(likePage - 1, 0)) }} />
                        <div className="activity-grid">
                            <AnimatePresence custom={direction} mode="popLayout">
                                <motion.div
                                    key={likePage}
                                    custom={direction}
                                    variants={variants}
                                    initial="enter"
                                    animate="center"
                                    exit="exit"
                                    transition={{ duration: 0.5 }}
                                    className="activity-slider"
                                    style={{ display: 'flex', gap: '1rem' }}
                                >
                                    <>
                                    {paginate(profile.myLikes, likePage).map(like => (
                                        <div key={like.id} className="activity-card" onClick={() => navigate(`/board/${like.id}`)}>
                                            {like.title}
                                        </div>
                                    ))}
                                    </>
                                </motion.div>
                            </AnimatePresence>
                        </div>
                        <ArrowButton direction="right" onClick={() => { setDirection(1); setLikePage(likePage + 1) }} />
                    </div>
                </div>
            </div>
        </div>
    );
}
