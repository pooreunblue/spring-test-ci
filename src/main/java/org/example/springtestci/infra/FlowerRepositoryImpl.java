package org.example.springtestci.infra;

import org.example.springtestci.domain.Flower;
import org.example.springtestci.domain.FlowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FlowerRepositoryImpl implements FlowerRepository {

    private final FlowerJpaRepository flowerJpaRepository;

    @Override
    public long count() {
        // JPA 저장소에 위임해 전체 개수 조회
        return flowerJpaRepository.count();
    }

    @Override
    public Flower save(Flower flower) {
        // 도메인 객체를 엔티티로 변환 후 저장
        FlowerJpaEntity savedEntity = flowerJpaRepository.save(toEntity(flower));
        return toDomain(savedEntity);
    }

    @Override
    public List<Flower> findAll() {
        // 엔티티 목록을 도메인 객체 목록으로 변환
        return flowerJpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private FlowerJpaEntity toEntity(Flower flower) {
        // 도메인 꽃 객체를 저장용 엔티티로 변환
        return new FlowerJpaEntity(flower.name(), flower.color(), flower.price());
    }

    private Flower toDomain(FlowerJpaEntity flowerJpaEntity) {
        // 엔티티를 도메인 꽃 객체로 변환
        return new Flower(flowerJpaEntity.getName(), flowerJpaEntity.getColor(), flowerJpaEntity.getPrice());
    }
}
