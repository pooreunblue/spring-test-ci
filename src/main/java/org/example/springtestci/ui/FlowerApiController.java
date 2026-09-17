package org.example.springtestci.ui;

import lombok.RequiredArgsConstructor;
import org.example.springtestci.app.FlowerUseCase;
import org.example.springtestci.domain.Flower;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flowers")
@RequiredArgsConstructor
public class FlowerApiController {
    private final FlowerUseCase flowerUseCase;

    @GetMapping("/count")
    public long count() {
        // 유스케이스에 위임해 전체 개수 조회
        return flowerUseCase.count();
    }

    @PostMapping
    public FlowerDTO save(@RequestBody FlowerDTO flowerDto) {
        // DTO를 도메인으로 변환 후 저장 위임
        Flower savedFlower = flowerUseCase.save(toDomain(flowerDto));
        return toDto(savedFlower);
    }

    @GetMapping
    public List<FlowerDTO> findAll() {
        // 유스케이스의 도메인 목록을 DTO 목록으로 변환
        return flowerUseCase.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private Flower toDomain(FlowerDTO flowerDto) {
        // 요청 DTO를 도메인 꽃 객체로 변환
        return new Flower(flowerDto.name(), flowerDto.color(), flowerDto.price());
    }

    private FlowerDTO toDto(Flower flower) {
        // 도메인 꽃 객체를 응답 DTO로 변환
        return new FlowerDTO(flower.name(), flower.color(), flower.price());
    }
}
