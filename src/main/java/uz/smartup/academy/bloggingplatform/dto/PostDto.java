package uz.smartup.academy.bloggingplatform.dto;

import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;

@Getter
public class PostDto {

    private int id;

    private String title;

    private byte[] photo;

    private String content;

    private LocalDate createdAt;

    private long likesCount;

    public PostDto() {}

    public PostDto(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.content = builder.content;
        this.photo = builder.photo;
        this.createdAt = builder.createdAt;
        this.likesCount = builder.likesCount;
    }

    public static class Builder {
        private int id;

        private String title;

        private byte[] photo;

        private String content;

        private LocalDate createdAt;

        private long likesCount;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder photo(byte[] photo) {
            this.photo = photo;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder createdAt(LocalDate createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder likesCount(long likesCount) {
            this.likesCount = likesCount;
            return this;
        }

        public PostDto build() {
            return new PostDto(this);
        }

    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", photo=" + Arrays.toString(photo) +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", likesCount=" + likesCount +
                '}';
    }
}
