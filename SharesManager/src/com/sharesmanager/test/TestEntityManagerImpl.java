package com.sharesmanager.test;

import com.sharesmanager.main.*;
import static org.junit.Assert.*;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;


public class TestEntityManagerImpl {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM uuuu");
	private static List<Entity> entities = new ArrayList<>();
	private static EntityManagerImpl em = null;
	
	@BeforeClass
	public static void init() {

		System.out.println("Populating sample data...");
	
		Entity entity1 = new Entity("foo");
		entity1.setAgreedFx(0.50f);
		entity1.setCurrency("SGP");
		entity1.setUnits(200);
		entity1.setPricePerUnit(100.25f);
		entity1.setInstruction("B");
		LocalDate date = LocalDate.parse("02 Jan 2016", formatter);				
		entity1.setSettlementDate(date);
				
		Entity entity2 = new Entity("bar");
		entity2.setAgreedFx(0.50f);
		entity2.setCurrency("SGP");
		entity2.setUnits(20);
		entity2.setPricePerUnit(100.25f);
		entity2.setInstruction("S");
		entity2.setSettlementDate(LocalDate.parse("04 Jan 2016", formatter));

		Entity entity3 = new Entity("entity3");
		entity3.setAgreedFx(0.50f);
		entity3.setCurrency("SGP");
		entity3.setUnits(45);
		entity3.setPricePerUnit(100.25f);
		entity3.setInstruction("S");
		entity3.setSettlementDate(LocalDate.parse("04 Jan 2017", formatter));

		Entity entity4 = new Entity("entity4");
		entity4.setAgreedFx(0.50f);
		entity4.setCurrency("SGP");
		entity4.setUnits(450);
		entity4.setPricePerUnit(80.50f);
		entity4.setInstruction("S");
		entity4.setSettlementDate(LocalDate.parse("04 Jan 2017", formatter));
		
		Entity entity5 = new Entity("entity5");
		entity5.setAgreedFx(0.90f);
		entity5.setCurrency("SAR");
		entity5.setUnits(450);
		entity5.setPricePerUnit(80.50f);
		entity5.setInstruction("B");
		entity5.setSettlementDate(LocalDate.parse("12 Sep 2017", formatter));
			
		Entity entity6 = new Entity("entity6");
		entity6.setAgreedFx(0.20f);
		entity6.setCurrency("SGP");
		entity6.setUnits(450);
		entity6.setPricePerUnit(80.50f);
		entity6.setInstruction("B");
		entity6.setSettlementDate(LocalDate.parse("08 Mar 2017", formatter));
				
		Entity entity7 = new Entity("entity7");
		entity7.setAgreedFx(0.10f);
		entity7.setCurrency("AED");
		entity7.setUnits(1000);
		entity7.setPricePerUnit(4.0f);
		entity7.setInstruction("S");
		entity7.setSettlementDate(LocalDate.parse("08 Sep 2017", formatter));
		
		entities.add(entity1);
		entities.add(entity2);
		entities.add(entity3);
		entities.add(entity4);
		entities.add(entity5);
		entities.add(entity6);
		entities.add(entity7);

		em = new EntityManagerImpl(entities);
		em.displayCombinedReport();
	}

	@Test
	public void testEntityFields() {
		Entity entity = entities.get(5); //entity6 
		assertEquals(450, entity.getUnits());
		assertEquals("SGP", entity.getCurrency());
		assertEquals("B", entity.getInstruction());
		assertEquals("entity6", entity.getId());			
	}

	@Test
	public void testProcessEntities() {
		assertEquals(4, em.getEntitiesIncome().size());
		assertEquals(3, em.getEntitiesOutgoing().size());		
	}

	@Test
	public void testAdjustSettlementDate() {
		Entity entity = new Entity("Friday-AED");		
		entity.setCurrency("AED");				
		entity.setSettlementDate(LocalDate.parse("18 Aug 2017", formatter)); // Friday + AED
		Entity adjustedEntity = em.adjustSettlementDate(entity);
		String date = adjustedEntity.getSettlementDate().toString();
		assertEquals("2017-08-20", date); // skip two days ahead to the first working day					
	
		Entity entity2 = new Entity("Sunday-SGP");		
		entity2.setCurrency("SGP");				
		entity2.setSettlementDate(LocalDate.parse("10 Sep 2017", formatter));
		Entity adjustedEntity2 = em.adjustSettlementDate(entity2);
		String date2 = adjustedEntity2.getSettlementDate().toString();
		assertEquals("2017-09-11", date2); // skip one day ahead to the first working day
	}
	
	@Test
	public void testDailyTradeAmountSumProcessing() {
		System.out.println("Unit test: DailyTradeAmountSumProcessing()");
		List<Entity> entitiesTest = new ArrayList<>();
		entitiesTest.add(entities.get(2)); // entity 3, "entity3", income (S), "04 Jan 2017", income: 2255,625
		entitiesTest.add(entities.get(3)); // entity 4, "entity4", income (S), "04 Jan 2017", income: 18112,5
		double sum = em.processDailySumOfUSDAmountOfATrade(entitiesTest, "S");
		assertEquals(20368.125, sum, 0.01);
	}
	@Test
	public void testCreateEntityIncomeReport() {
		List<Entity> entitiesIncome = em.getEntitiesIncome();
				
		for (int i = 0; i < entitiesIncome.size() - 1; ++i) {
			Entity prev = entitiesIncome.get(i);
			Entity next = entitiesIncome.get(i + 1);
			double prevAmount = prev.getUSDAmountOfATrade();
			double nextAmount = next.getUSDAmountOfATrade();			
			assertTrue(prevAmount >= nextAmount); // descending order, highest outgoing first			
		}
	}
	
	@Test
	public void testCreateEntityOutgoingReport() {
		System.out.println("testCreateEntityOutgoingReport");
		List<Entity> entitiesOutgoing = em.getEntitiesOutgoing();
	
		for (int i = 0; i < entitiesOutgoing.size() - 1; ++i) {
			Entity prev = entitiesOutgoing.get(i);
			Entity next = entitiesOutgoing.get(i + 1);
			double prevAmount = prev.getUSDAmountOfATrade();
			double nextAmount = next.getUSDAmountOfATrade();			
			assertTrue(prevAmount >= nextAmount); // descending order, highest outgoing first		
		}	
	}
}
