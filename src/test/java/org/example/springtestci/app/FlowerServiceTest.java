package org.example.springtestci.app;

import org.example.springtestci.domain.Flower;
import org.example.springtestci.domain.FlowerRepository;
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

// FlowerService가 FlowerRepository에 위임하는지 검증하는 단위 테스트
@ExtendWith(MockitoExtension.class)
class FlowerServiceTest {

    @Mock
    private FlowerRepository flowerRepository;

    private FlowerService flowerService;

    @BeforeEach
    void setUp() {
        // 목킹된 저장소를 주입한 서비스 준비
        flowerService = new FlowerService(flowerRepository);
    }

    @Test
    @DisplayName("count 호출 시 저장소의 count 결과를 그대로 반환한다")
    void given_repository_count_when_count_then_delegates_to_repository() {
        // given: 저장소가 개수 5를 반환하도록 설정
        given(flowerRepository.count()).willReturn(5L);

        // when: 서비스에 개수 조회 요청
        long count = flowerService.count();

        // then: 저장소 결과가 그대로 반환되고 위임이 호출됨
        assertThat(count).isEqualTo(5L);
        verify(flowerRepository).count();
    }

    @Test
    @DisplayName("save 호출 시 저장소에 위임하고 결과를 반환한다")
    void given_flower_when_save_then_delegates_to_repository() {
        // given: 저장 요청 시 동일한 꽃을 반환하도록 설정
        Flower flower = new Flower("장미", "빨강", 5000);
        given(flowerRepository.save(flower)).willReturn(flower);

        // when: 서비스에 꽃 저장 요청
        Flower savedFlower = flowerService.save(flower);

        // then: 저장소 결과가 그대로 반환되고 위임이 호출됨
        assertThat(savedFlower).isEqualTo(flower);
        verify(flowerRepository).save(flower);
    }

    @Test
    @DisplayName("findAll 호출 시 저장소의 목록을 그대로 반환한다")
    void given_repository_flowers_when_findAll_then_delegates_to_repository() {
        // given: 저장소가 꽃 목록을 반환하도록 설정
        Flower flower = new Flower("백합", "흰색", 4000);
        given(flowerRepository.findAll()).willReturn(List.of(flower));

        // when: 서비스에 전체 목록 조회 요청
        List<Flower> flowers = flowerService.findAll();

        // then: 저장소 결과가 그대로 반환되고 위임이 호출됨
        assertThat(flowers).containsExactly(flower);
        verify(flowerRepository).findAll();
    }
}
