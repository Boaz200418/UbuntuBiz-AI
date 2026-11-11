package africa.ubuntubiz.ai

import africa.ubuntubiz.ai.domain.*
import org.junit.Assert.*
import org.junit.Test

class GeneratorTest {
    private val founder=Entrepreneur("Rudo","Rudo Gardens","Zimbabwe","Agriculture","I learned to grow herbs with my grandmother, using our small family garden.","Fresh herbs and seedlings","Supply three local kitchens","Growing herbs")
    @Test fun storyPreservesTestimonyAndAuthorship() {
        val sections=LocalGenerator.story(founder)
        assertEquals(6,sections.size)
        assertTrue(sections[0].body.contains("“${founder.story}”"))
        assertTrue(sections[0].body.contains("Story by Rudo"))
        assertFalse(LocalGenerator.plain(sections).contains("award-winning"))
    }
    @Test fun quotationUsesExactDecimalArithmetic() {
        val text=LocalGenerator.plain(LocalGenerator.document("Quotation",founder,MoneyInputs(quantity="3",unitPrice="0.10")))
        assertTrue(text.contains("Total before tax: ZAR 0.30"))
        assertTrue(text.contains("Tax: not calculated"))
    }
    @Test fun budgetAddsOnlyEnteredEstimates() {
        val text=LocalGenerator.plain(LocalGenerator.document("Startup budget",founder,MoneyInputs(equipment="10.10",stock="20.20",marketing="0",reserve="30.30")))
        assertTrue(text.contains("Total estimate: ZAR 60.60"))
    }
    @Test fun invalidMoneyIsRejected() {
        listOf("-1","abc","1,000","1.999","1e9","").forEach { invalid ->
            assertTrue(runCatching { LocalGenerator.document("Quotation",founder,MoneyInputs(unitPrice=invalid)) }.isFailure)
        }
        assertTrue(runCatching { LocalGenerator.document("Quotation",founder,MoneyInputs(quantity="0")) }.isFailure)
    }
    @Test fun allDocumentsAreMeaningfulAndLabelled() {
        Catalog.documentTypes.forEach { type ->
            val s=LocalGenerator.document(type,founder,MoneyInputs())
            assertTrue(s.size>=4);assertTrue(s.first().body.contains(founder.business))
            assertTrue(s.last().body.contains("not verified legal, tax or financial advice"))
        }
    }
    @Test fun missingFounderInputDoesNotGenerate() {
        assertNotNull(Entrepreneur().validation())
        assertTrue(runCatching { LocalGenerator.story(Entrepreneur()) }.isFailure)
        assertTrue(runCatching { LocalGenerator.document("Business plan",Entrepreneur(),MoneyInputs()) }.isFailure)
    }
    @Test fun phrasebookNeverFabricatesUnknownTranslation() {
        assertNull(Phrasebook.translate("My business exports tomatoes.","English","Shona"))
        Catalog.languages.forEach { to -> assertNotNull(Phrasebook.translate(Phrasebook.source,"English",to)) }
        assertEquals("Hello",Phrasebook.translate("Hello","English","English"))
        assertNull(Phrasebook.translate("","English","Swahili"))
    }
    @Test fun reverseGreetingWorks() {
        assertEquals(Phrasebook.source,Phrasebook.translate(Phrasebook.example("isiZulu"),"isiZulu","English"))
    }
}
