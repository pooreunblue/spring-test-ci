package org.example.springtestci.ui;

import org.example.springtestci.app.FlowerUseCase;
import org.example.springtestci.domain.Flower;
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

// FlowerApiController가 FlowerUseCase에 위임해 DTO를 주고받는지 검증하는 단위 테스트
@ExtendWith(MockitoExtension.class)
class FlowerApiControllerTest {

    @Mock
    private FlowerUseCase flowerUseCase;

    private FlowerApiController flowerApiController;

    @BeforeEach
    void setUp() {
        // 목킹된 유스케이스를 주입한 컨트롤러 준비
        flowerApiController = new FlowerApiController(flowerUseCase);
    }

    @Test
    @DisplayName("count 호출 시 유스케이스의 count 결과를 그대로 반환한다")
    void given_usecase_count_when_count_then_returns_same_value() {
        // given: 유스케이스가 개수 5를 반환하도록 설정
        given(flowerUseCase.count()).willReturn(5L);

        // when: 컨트롤러에 개수 조회 요청
        long count = flowerApiController.count();

        // then: 유스케이스 결과가 그대로 반환됨
        assertThat(count).isEqualTo(5L);
    }

    @Test
    @DisplayName("save 호출 시 DTO를 도메인으로 변환해 위임하고 DTO로 반환한다")
    void given_flower_dto_when_save_then_delegates_and_returns_dto() {
        // given: 저장 요청 DTO와 도메인 변환 결과 준비
        FlowerDTO requestDto = new FlowerDTO("장미", "빨강", 5000);
        Flower flower = new Flower(requestDto.name(), requestDto.color(), requestDto.price());
        given(flowerUseCase.save(flower)).willReturn(flower);

        // when: 컨트롤러에 꽃 저장 요청
        FlowerDTO savedDto = flowerApiController.save(requestDto);

        // then: 저장 결과가 DTO로 변환되어 반환되고 위임이 호출됨
        assertThat(savedDto).isEqualTo(requestDto);
        verify(flowerUseCase).save(flower);
    }

    @Test
    @DisplayName("findAll 호출 시 유스케이스의 목록을 DTO 목록으로 변환해 반환한다")
    void given_usecase_flowers_when_findAll_then_returns_dto_list() {
        // given: 유스케이스가 도메인 꽃 목록을 반환하도록 설정
        Flower flower = new Flower("백합", "흰색", 4000);
        given(flowerUseCase.findAll()).willReturn(List.of(flower));

        // when: 컨트롤러에 전체 목록 조회 요청
        List<FlowerDTO> flowerDtos = flowerApiController.findAll();

        // then: 도메인 목록이 DTO 목록으로 변환되어 반환됨
        assertThat(flowerDtos)
                .containsExactly(new FlowerDTO(flower.name(), flower.color(), flower.price()));
    }
}
