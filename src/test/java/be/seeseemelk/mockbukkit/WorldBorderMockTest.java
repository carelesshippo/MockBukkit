package be.seeseemelk.mockbukkit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class WorldBorderMockTest
{
	private WorldBorder worldBorderMock;
	private World world;
	private ServerMock server;

	@BeforeEach
	public void setUp()
	{
		server = MockBukkit.mock();
		world = new WorldMock();
		worldBorderMock = world.getWorldBorder();
	}

	@AfterEach
	public void tearDown()
	{
		MockBukkit.unmock();
	}

	@Test
	void reset()
	{
		worldBorderMock.reset();

		assertEquals(6.0E7, worldBorderMock.getSize());
		assertEquals(0.2, worldBorderMock.getDamageAmount());
		assertEquals(5.0, worldBorderMock.getDamageBuffer());
		assertEquals(5, worldBorderMock.getWarningDistance());
		assertEquals(15, worldBorderMock.getWarningTime());
		assertEquals(new Location(world, 0, 0, 0), worldBorderMock.getCenter());
	}

	@Test
	void setSize()
	{
		worldBorderMock.setSize(10);

		assertEquals(10, worldBorderMock.getSize());
	}

	@Test
	void setSize_OverTimeGoingDown_CompleteAtFullTime()
	{
		worldBorderMock.setSize(100);
		worldBorderMock.setSize(50, 10);

		server.getScheduler().performTicks(10 * 20);

		assertEquals(50, worldBorderMock.getSize());
	}

	@Test
	void setSize_OverTimeGoingUp_CompleteAtFullTime()
	{
		worldBorderMock.setSize(50);
		worldBorderMock.setSize(100, 10);

		server.getScheduler().performTicks(10 * 20);

		assertEquals(100, worldBorderMock.getSize());
	}

	@Test
	void setSize_OverTime_HalfwayAtHalfTime()
	{
		worldBorderMock.setSize(100);
		worldBorderMock.setSize(50, 10);

		server.getScheduler().performTicks(5 * 20);

		assertEquals(75, worldBorderMock.getSize());
	}


	@Test
	void setCenterLocation()
	{
		worldBorderMock.setCenter(new Location(null, 10, 0, 10));

		assertEquals(new Location(world, 10, 0, 10), worldBorderMock.getCenter());
	}

	@Test
	void setCenterXZ()
	{
		worldBorderMock.setCenter(10, 10);

		assertEquals(new Location(world, 10, 0, 10), worldBorderMock.getCenter());
	}

	@Test
	void setDamageBuffer()
	{
		worldBorderMock.setDamageBuffer(10.0);

		assertEquals(10.0, worldBorderMock.getDamageBuffer());
	}

	@Test
	void setDamageAmount()
	{
		worldBorderMock.setDamageAmount(1);

		assertEquals(1, worldBorderMock.getDamageAmount());
	}

	@Test
	void setWarningTime()
	{
		worldBorderMock.setWarningTime(10);

		assertEquals(10, worldBorderMock.getWarningTime());
	}

	@Test
	void setWarningDistance()
	{
		worldBorderMock.setWarningDistance(10);

		assertEquals(10, worldBorderMock.getWarningDistance());
	}

	@Test
	void isInside_Null_ExceptionThrown()
	{
		assertThrows(IllegalArgumentException.class, () ->
		{
			worldBorderMock.isInside(null);
		});
	}

	@Test
	void isInside_InsideLocation_True()
	{
		worldBorderMock.setSize(100);

		assertTrue(worldBorderMock.isInside(new Location(world, 0, 0, 0)));
	}

	@Test
	void isInside_InsideLocationWrongWorld_False()
	{
		worldBorderMock.setSize(100);

		assertFalse(worldBorderMock.isInside(new Location(new WorldMock(), 0, 0, 0)));
	}

	@Test
	void isInside_OutsideLocation_False()
	{
		worldBorderMock.setSize(100);

		assertFalse(worldBorderMock.isInside(new Location(world, 101, 0, 101)));
	}
}
