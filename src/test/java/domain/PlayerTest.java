package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerTest {

	@Test
	public void getCardTypeCount_withNullCardType_throwsNullPointerException() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.getCountOfCardType(null)).andThrow(new NullPointerException("CardType cannot be null"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(NullPointerException.class, () -> player.getCardTypeCount(null));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void getCardTypeCount_withCardNotInHand_returnsZero(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.getCountOfCardType(testCardType)).andReturn(0);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertEquals (0, player.getCardTypeCount(testCardType));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void getCardTypeCount_withOneCardInHand_returnsOne(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.getCountOfCardType(testCardType)).andReturn(1);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertEquals (1, player.getCardTypeCount(testCardType));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void getCardTypeCount_withTwoCardsInHand_returnsTwo(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.getCountOfCardType(testCardType)).andReturn(2);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertEquals (2, player.getCardTypeCount(testCardType));

		EasyMock.verify(mockHand);
	}
}
