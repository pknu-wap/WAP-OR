package com.wap.wapor.dto;

public class KakaoUserResponse {
    private Long id;
    private KakaoAccount kakao_account;
    public  getKakao_account() {
        return kakao_account;
    }

    public void setKakao_account(KakaoAccount kakao_account) {
        this.kakao_account = kakao_account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    // Getter, Setter

    public static class KakaoAccount {
        private Profile profile;
        private String email;
        public  getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }



        // Getter, Setter

        public static class Profile {
            private String nickname;
            private String profile_image_url;
            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getProfile_image_url() {
                return profile_image_url;
            }

            public void setProfile_image_url(String profile_image_url) {
                this.profile_image_url = profile_image_url;
            }



            // Getter, Setter
        }
    }
}
