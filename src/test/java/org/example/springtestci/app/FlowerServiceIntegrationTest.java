package org.example.springtestci.app;

import org.example.springtestci.domain.Flower;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// 실제 스프링 컨텍스트와 DB를 사용해 FlowerService를 검증하는 통합 테스트
@SpringBootTest
@Transactional
class FlowerServiceIntegrationTest {

    @Autowired
    private FlowerUseCase flowerUseCase;

    @Test
    @DisplayName("저장된 꽃이 없으면 count는 0을 반환한다")
    void given_no_flowers_when_count_then_returns_zero() {
        // when: 초기 상태에서 개수 조회
        long count = flowerUseCase.count();

        // then: 저장된 꽃이 없으므로 0이어야 함
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("꽃을 저장하면 실제 DB에 반영되어 count가 증가한다")
    void given_flower_when_save_then_persisted_and_count_increases() {
        // given: 저장할 꽃 객체 준비
        Flower flower = new Flower("장미", "빨강", 5000);

        // when: 서비스에 꽃 저장 요청
        Flower savedFlower = flowerUseCase.save(flower);

        // then: 저장 결과와 개수가 실제 DB에 반영됨
        assertThat(savedFlower).isEqualTo(flower);
        assertThat(flowerUseCase.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("꽃을 여러 개 저장하면 findAll에 모두 포함된다")
    void given_multiple_flowers_when_save_then_findAll_returns_all() {
        // given: 저장할 꽃 두 개 준비
        Flower rose = new Flower("장미", "빨강", 5000);
        Flower lily = new Flower("백합", "흰색", 4000);

        // when: 서비스에 꽃을 순서대로 저장
        flowerUseCase.save(rose);
        flowerUseCase.save(lily);

        // then: 전체 목록에 저장한 꽃이 모두 포함되어야 함
        List<Flower> flowers = flowerUseCase.findAll();
        assertThat(flowers).containsExactlyInAnyOrder(rose, lily);
    }
}
