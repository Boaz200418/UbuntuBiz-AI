package africa.ubuntubiz.ai.domain

import java.math.BigDecimal
import java.math.RoundingMode

/** Deterministic offline drafting. User testimony stays quoted and attributed. */
object LocalGenerator {
    fun story(p: Entrepreneur): List<ContentSection> {
        require(p.validation() == null) { p.validation() ?: "Invalid input" }
        return listOf(
            ContentSection("Entrepreneur story", "${p.business}: a story from ${p.country}\n\nMy name is ${p.name}, and I am building ${p.business} in ${p.country}. My work is in ${p.category}.\n\nIn my own words\n“${p.story.trim()}”\n\nWhat I offer\n${p.products.trim()}\n\nWhat I am working towards\n${p.goals.trim()}\n\nStory by ${p.name}. Review this draft before sharing."),
            ContentSection("Short business biography", "${p.name} is the entrepreneur behind ${p.business}, a ${p.category} business in ${p.country}. The business offers ${p.products.trim().trimEnd('.')}."),
            ContentSection("Business introduction", "Hello, I’m ${p.name}, founder of ${p.business} in ${p.country}. Here is what we offer: ${p.products.trim()}\nOur next goal: ${p.goals.trim()}"),
            ContentSection("Social media caption", "Every business starts with a story. Here is mine:\n\n“${p.story.trim()}”\n\nDiscover ${p.business} in ${p.country}.\n${p.products.trim()}\n\n#UbuntuBiz #AfricanEntrepreneurs"),
            ContentSection("Product description", "Discover the offering from ${p.business}:\n\n${p.products.trim()}\n\nCreated by ${p.name} in ${p.country}. Ask us about specifications, availability and pricing."),
            ContentSection("Marketing message", "Meet ${p.business}. Explore ${p.products.trim().trimEnd('.')}.\n\nWe are working towards this goal: ${p.goals.trim()}\n\nConnect with ${p.name} to find out more.")
        )
    }
    private fun amount(s: String): BigDecimal {
        require(Regex("\\d{1,9}(\\.\\d{1,2})?").matches(s)) { "Use positive numbers with up to 2 decimal places (for example 150.00)." }
        return s.toBigDecimal().setScale(2, RoundingMode.HALF_UP)
    }
    fun document(type: String, p: Entrepreneur, m: MoneyInputs): List<ContentSection> {
        require(type in Catalog.documentTypes) { "Choose a document type." }
        require(p.business.isNotBlank() && p.name.isNotBlank() && p.products.isNotBlank()) { "Add your name, business name and products in My Story first." }
        val currency = m.currency.trim().uppercase()
        require(Regex("[A-Z]{3}").matches(currency)) { "Enter a 3-letter currency code, such as ZAR, USD or KES." }
        val opening = ContentSection(type, "${p.business}\nPrepared by ${p.name} • ${p.country}\nLOCAL TEMPLATE • DRAFT FOR REVIEW\nUser-supplied information; no credentials or market claims verified.")
        val sections = when(type) {
            "Business plan" -> listOf(
                ContentSection("01 / Business overview", "${p.business} operates in ${p.category}.\nOffering: ${p.products}\nFounder background, in their own words: ${p.story}"),
                ContentSection("02 / Customers & validation", "Working assumptions to validate:\n• Define one customer group and their problem.\n• Interview five potential customers.\n• Test one small paid order before expanding.\nRecord evidence and adjust the offer."),
                ContentSection("03 / Operations & milestones", "Founder goal: ${p.goals.ifBlank { "Add a specific goal in My Story." }}\nWeek 1: list suppliers and requirements.\nWeek 2: test costs and production.\nWeek 3: collect customer feedback.\nWeek 4: review sales, expenses and next steps."),
                ContentSection("04 / Financial planning", "Prepare a Startup budget using your own estimates. Confirm prices, demand, taxes and local registration requirements before committing funds. No revenue forecast has been invented."))
            "Company profile" -> listOf(ContentSection("Who we are", "${p.business} • ${p.country}\nFounder: ${p.name}\nSector: ${p.category}\nSkills: ${p.skills.ifBlank { "To be supplied" }}"), ContentSection("Our story", p.story.ifBlank { "Add your founder story." }), ContentSection("Products & direction", "${p.products}\n\nOur goal: ${p.goals}\n\nContact details and registration information: add and verify before sending."))
            "Business proposal" -> listOf(ContentSection("Proposed offering", p.products), ContentSection("Objective", p.goals.ifBlank { "Define the client's objective before sending." }), ContentSection("Delivery approach", "1. Agree scope and acceptance criteria with the client.\n2. Confirm materials, timelines and milestones.\n3. Deliver a small initial milestone for review.\n4. Obtain written acceptance before final handover."), ContentSection("Commercial details to complete", "Client: [enter client]\nDelivery date: [agree date]\nPrice and currency: [confirm with quotation]\nExclusions, payment terms and local legal requirements: [review with qualified support]"))
            "Quotation" -> {
                val q = amount(m.quantity); require(q > BigDecimal.ZERO) { "Quantity must be greater than zero." }
                val unit = amount(m.unitPrice); val total = q.multiply(unit).setScale(2, RoundingMode.HALF_UP)
                listOf(ContentSection("Quote details", "Client: [add recipient]\nItem / service: ${p.products}\nQuantity: $q\nUnit price: $currency $unit\nSubtotal: $currency $total\nTax: not calculated; confirm applicable tax.\nTotal before tax: $currency $total"),ContentSection("Terms to confirm", "Validity, delivery, payment terms and tax treatment must be agreed before sending. This draft is not an invoice or accepted contract."))
            }
            "Startup budget" -> {
                val values = listOf(amount(m.equipment), amount(m.stock), amount(m.marketing), amount(m.reserve))
                val names = listOf("Equipment", "Initial stock / materials", "Marketing", "Working capital reserve")
                listOf(ContentSection("Your estimated startup costs", names.zip(values).joinToString("\n") { (n,v) -> "$n: $currency $v" } + "\n\nTotal estimate: $currency ${values.fold(BigDecimal.ZERO, BigDecimal::add).setScale(2)}"),ContentSection("Assumptions & next steps", "These are user-entered estimates, not supplier quotes. Check actual prices, recurring costs, tax, transport and licences. Validate each item before spending."))
            }
            else -> listOf(ContentSection("Positioning", "Offering: ${p.products}\nFounder story: ${p.story}\nGoal: ${p.goals}"),ContentSection("30-day marketing experiment", "Week 1: choose one audience; ask five people about their needs.\nWeek 2: create three posts using your own product photos and attributed story.\nWeek 3: test one channel with a small budget you can afford.\nWeek 4: compare enquiries, orders, costs and customer feedback."),ContentSection("Measure & improve", "Track enquiries, qualified leads, paid orders, repeat customers and cost per order. Ask consent before using customer stories. Do not claim results you have not measured."))
        }
        return listOf(opening) + sections + ContentSection("Review before use", "Generated locally from templates. This is an example draft, not verified legal, tax or financial advice. You retain authorship of your original story. Nothing is published automatically.")
    }
    fun plain(sections: List<ContentSection>) = sections.joinToString("\n\n") { "${it.title}\n${it.body}" }
}
object Phrasebook {
    const val source = "Welcome to my business."
    private val translations = mapOf("English" to source, "Shona" to "Mauya kubhizimisi rangu.", "isiXhosa" to "Wamkelekile kwishishini lam.", "isiZulu" to "Siyakwamukela ebhizinisini lami.", "Swahili" to "Karibu kwenye biashara yangu.", "French" to "Bienvenue dans mon entreprise.", "Portuguese" to "Bem-vindo ao meu negócio.")
    fun translate(text: String, from: String, to: String): String? {
        if (from !in Catalog.languages || to !in Catalog.languages || text.isBlank()) return null
        if (from == to) return text
        if (!text.trim().equals(translations[from], ignoreCase = true)) return null
        return translations[to]
    }
    fun example(language: String) = translations[language] ?: source
}
