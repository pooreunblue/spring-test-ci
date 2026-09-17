package org.example.springtestci;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// CI 파이프라인의 실패 감지 동작을 확인하기 위한 테스트
class DeliberatelyFailingTest {

    @Test
    @DisplayName("의도적으로 실패하도록 작성된 테스트다")
    void given_wrong_expectation_when_asserted_then_fails() {
        // given: 실제 값과 다른 기대값을 준비
        int actual = 1;

        // when & then: 일부러 틀린 값과 비교해 실패 유도
        assertThat(actual).isEqualTo(2);
    }
}
