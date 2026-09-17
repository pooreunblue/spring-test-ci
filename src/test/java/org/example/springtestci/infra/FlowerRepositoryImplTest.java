package org.example.springtestci.infra;

import org.example.springtestci.domain.Flower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

// FlowerRepositoryImpl의 JPA 위임 및 매핑 동작을 검증하는 단위 테스트
@ExtendWith(MockitoExtension.class)
class FlowerRepositoryImplTest {

    @Mock
    private FlowerJpaRepository flowerJpaRepository;

    private FlowerRepositoryImpl flowerRepositoryImpl;

    @BeforeEach
    void setUp() {
        // 매 테스트마다 목킹된 JPA 저장소로 구현체 생성
        flowerRepositoryImpl = new FlowerRepositoryImpl(flowerJpaRepository);
    }

    @Test
    @DisplayName("저장된 꽃이 없으면 count는 0을 반환한다")
    void given_empty_repository_when_count_then_returns_zero() {
        // given: JPA 저장소가 0을 반환하도록 설정
        given(flowerJpaRepository.count()).willReturn(0L);

        // when: 저장소에 개수 조회 요청
        long count = flowerRepositoryImpl.count();

        // then: 개수가 0이어야 함
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("꽃을 저장하면 저장된 꽃 객체를 반환한다")
    void given_flower_when_save_then_returns_saved_flower() {
        // given: 저장 시 전달받은 엔티티를 그대로 반환하도록 설정
        Flower flower = new Flower("장미", "빨강", 5000);
        given(flowerJpaRepository.save(any(FlowerJpaEntity.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when: 저장소에 꽃 저장 요청
        Flower savedFlower = flowerRepositoryImpl.save(flower);

        // then: 저장한 꽃과 값이 동일한 도메인 객체가 반환됨
        assertThat(savedFlower).isEqualTo(flower);
    }

    @Test
    @DisplayName("꽃을 저장하면 count가 1 증가한다")
    void given_flower_when_save_then_count_increases_by_one() {
        // given: 저장 이후 개수가 1이 되도록 설정
        Flower flower = new Flower("튤립", "노랑", 3000);
        given(flowerJpaRepository.save(any(FlowerJpaEntity.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(flowerJpaRepository.count()).willReturn(1L);

        // when: 꽃 저장 후 개수 조회
        flowerRepositoryImpl.save(flower);
        long count = flowerRepositoryImpl.count();

        // then: 개수는 1이어야 함
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("꽃을 여러 번 저장하면 count는 저장 횟수만큼 증가한다")
    void given_multiple_flowers_when_save_then_count_matches_saved_size() {
        // given: 저장 이후 개수가 2가 되도록 설정
        Flower rose = new Flower("장미", "빨강", 5000);
        Flower lily = new Flower("백합", "흰색", 4000);
        given(flowerJpaRepository.save(any(FlowerJpaEntity.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(flowerJpaRepository.count()).willReturn(2L);

        // when: 꽃 두 개를 순서대로 저장 후 개수 조회
        flowerRepositoryImpl.save(rose);
        flowerRepositoryImpl.save(lily);
        long count = flowerRepositoryImpl.count();

        // then: 개수는 2여야 함
        assertThat(count).isEqualTo(2L);
    }

    @Test
    @DisplayName("저장된 꽃이 없으면 findAll은 빈 목록을 반환한다")
    void given_empty_repository_when_findAll_then_returns_empty_list() {
        // given: JPA 저장소가 빈 목록을 반환하도록 설정
        given(flowerJpaRepository.findAll()).willReturn(List.of());

        // when: 저장소에 전체 목록 조회 요청
        List<Flower> flowers = flowerRepositoryImpl.findAll();

        // then: 빈 목록이 반환되어야 함
        assertThat(flowers).isEmpty();
    }

    @Test
    @DisplayName("꽃을 저장하면 findAll에 저장한 꽃이 포함된다")
    void given_flower_when_save_then_findAll_contains_saved_flower() {
        // given: 저장한 꽃과 동일한 엔티티가 목록에 포함되도록 설정
        Flower flower = new Flower("해바라기", "노랑", 2000);
        FlowerJpaEntity entity = new FlowerJpaEntity(flower.name(), flower.color(), flower.price());
        given(flowerJpaRepository.findAll()).willReturn(List.of(entity));

        // when: 저장소에 전체 목록 조회 요청
        List<Flower> flowers = flowerRepositoryImpl.findAll();

        // then: 목록에 저장한 꽃이 포함되어야 함
        assertThat(flowers).containsExactly(flower);
    }

    @Test
    @DisplayName("꽃을 여러 번 저장하면 findAll은 저장 순서대로 반환한다")
    void given_multiple_flowers_when_save_then_findAll_returns_in_order() {
        // given: 저장한 순서대로 엔티티 목록이 반환되도록 설정
        Flower rose = new Flower("장미", "빨강", 5000);
        Flower lily = new Flower("백합", "흰색", 4000);
        FlowerJpaEntity roseEntity = new FlowerJpaEntity(rose.name(), rose.color(), rose.price());
        FlowerJpaEntity lilyEntity = new FlowerJpaEntity(lily.name(), lily.color(), lily.price());
        given(flowerJpaRepository.findAll()).willReturn(List.of(roseEntity, lilyEntity));

        // when: 저장소에 전체 목록 조회 요청
        List<Flower> flowers = flowerRepositoryImpl.findAll();

        // then: 저장한 순서와 동일하게 반환되어야 함
        assertThat(flowers).containsExactly(rose, lily);
    }
}
