package com.wap.wapor.dto;

public class KakaoUserResponse {
    private Long id;
    private KakaoAccount kakao_account;

    // Getter, Setter

    public static class KakaoAccount {
        private Profile profile;
        private String email;

        // Getter, Setter

        public static class Profile {
            private String nickname;
            private String profile_image_url;

            // Getter, Setter
        }
    }
}
