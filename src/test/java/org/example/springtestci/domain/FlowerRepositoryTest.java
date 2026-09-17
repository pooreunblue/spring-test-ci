package org.example.springtestci.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

// FlowerRepository 인터페이스의 계약을 목 객체로 검증
@ExtendWith(MockitoExtension.class)
class FlowerRepositoryTest {

    @Mock
    private FlowerRepository flowerRepository;

    private Flower flower;

    @BeforeEach
    void setUp() {
        // 테스트에서 공통으로 사용할 꽃 객체 준비
        flower = new Flower("장미", "빨강", 5000);
    }

    @Test
    @DisplayName("꽃을 저장하면 저장된 꽃 객체를 반환한다")
    void given_flower_when_save_then_returns_saved_flower() {
        // given: 저장 호출 시 동일한 꽃 반환하도록 스텁 설정
        given(flowerRepository.save(flower)).willReturn(flower);

        // when: 저장소에 꽃 저장을 요청
        Flower savedFlower = flowerRepository.save(flower);

        // then: 반환된 꽃이 저장 요청한 꽃과 동일함을 검증
        assertThat(savedFlower).isEqualTo(flower);
        verify(flowerRepository).save(flower);
    }

    @Test
    @DisplayName("저장된 꽃이 없으면 개수는 0을 반환한다")
    void given_no_saved_flowers_when_count_then_returns_zero() {
        // given: 저장된 꽃이 없는 상태를 가정
        given(flowerRepository.count()).willReturn(0L);

        // when: 저장소에 꽃 개수를 조회
        long count = flowerRepository.count();

        // then: 개수가 0으로 반환됨을 검증
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("꽃이 저장되어 있으면 저장된 개수를 반환한다")
    void given_saved_flowers_when_count_then_returns_saved_count() {
        // given: 꽃 3개가 저장되어 있다고 가정
        given(flowerRepository.count()).willReturn(3L);

        // when: 저장소에 꽃 개수를 조회
        long count = flowerRepository.count();

        // then: 개수가 3으로 반환됨을 검증
        assertThat(count).isEqualTo(3L);
    }

    @Test
    @DisplayName("저장된 꽃이 없으면 빈 목록을 반환한다")
    void given_no_saved_flowers_when_findAll_then_returns_empty_list() {
        // given: 조회 결과가 빈 목록이도록 스텁 설정
        given(flowerRepository.findAll()).willReturn(List.of());

        // when: 저장소에 전체 꽃 목록을 조회
        List<Flower> flowers = flowerRepository.findAll();

        // then: 빈 목록이 반환됨을 검증
        assertThat(flowers).isEmpty();
    }

    @Test
    @DisplayName("꽃이 저장되어 있으면 저장된 꽃 목록을 반환한다")
    void given_saved_flowers_when_findAll_then_returns_flower_list() {
        // given: 꽃 목록이 존재하도록 스텁 설정
        given(flowerRepository.findAll()).willReturn(List.of(flower));

        // when: 저장소에 전체 꽃 목록을 조회
        List<Flower> flowers = flowerRepository.findAll();

        // then: 저장된 꽃이 포함되어 반환됨을 검증
        assertThat(flowers).containsExactly(flower);
    }
}
