package ktb.team5.ai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity(name = "tb_destination")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "tag")
    private String tags;

    public static Destination of(
            Media media,
            String name,
            String address,
            String description,
            Double latitude,
            Double longitude,
            List<String> tags
    ) {
        Destination dest = new Destination();
        dest.media = media;
        dest.name = name;
        dest.address = address;
        dest.description = description;
        dest.latitude = latitude;
        dest.longitude = longitude;
        dest.tags = toString(tags);
        return dest;
    }

    public void setTags(List<String> tags) {
        this.tags = toString(tags);
    }

    public List<String> getTags() {
        return fromString(this.tags);
    }

    private static String toString(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }

        return tags.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(","));
    }

    private static List<String> fromString(String tags) {
        if (tags == null || tags.isBlank()) {
            return List.of();
        }

        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
