package ktb.team5.ai.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String tag;

    public static Destination of(
            Media media,
            String name,
            String address,
            String description,
            Double latitude,
            Double longitude,
            String tag
    ) {
        Destination dest = new Destination();
        dest.media = media;
        dest.name = name;
        dest.address = address;
        dest.description = description;
        dest.latitude = latitude;
        dest.longitude = longitude;
        dest.tag = tag;
        return dest;
    }
}
