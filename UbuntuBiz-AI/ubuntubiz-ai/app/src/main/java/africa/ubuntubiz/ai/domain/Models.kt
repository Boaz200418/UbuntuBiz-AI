package africa.ubuntubiz.ai.domain

data class Entrepreneur(
    val name: String = "", val business: String = "", val country: String = "",
    val category: String = "", val story: String = "", val products: String = "",
    val goals: String = "", val skills: String = ""
) {
    fun validation(): String? = when {
        name.isBlank() -> "Please enter your name."
        business.isBlank() -> "Please enter your business name."
        country.isBlank() -> "Please enter your country."
        category.isBlank() -> "Please enter a business category."
        story.trim().length < 20 -> "Tell us a little more: your story needs at least 20 characters."
        products.isBlank() -> "Please describe your products or services."
        goals.isBlank() -> "Please add a business goal."
        else -> null
    }
}
data class ContentSection(val title: String, val body: String)
data class Business(val name: String, val owner: String, val country: String, val category: String, val description: String, val products: String, val initials: String)
data class Opportunity(val title: String, val kind: String, val description: String, val eligibility: String, val preparation: String)
data class MoneyInputs(val currency: String = "ZAR", val quantity: String = "10", val unitPrice: String = "150", val equipment: String = "2000", val stock: String = "1500", val marketing: String = "500", val reserve: String = "1000")
object Catalog {
    val businesses = listOf(
        Business("Mavambo Clay Studio", "Tariro Moyo", "Zimbabwe", "Craft & design", "Tariro makes hand-shaped ceramics inspired by the textures of everyday life in Harare.", "Tableware, planters and small-batch ceramic gifts", "TM"),
        Business("Langa Harvest", "Ayanda Dlamini", "South Africa", "Agriculture", "A Cape Town grower connecting neighbourhood kitchens with seasonal vegetables.", "Vegetable boxes, seedlings and growing workshops", "AD"),
        Business("Kijani Threads", "Amina Otieno", "Kenya", "Fashion", "A Nairobi sewing studio creating practical accessories from reclaimed textiles.", "Tote bags, aprons and custom textile accessories", "AO"),
        Business("Lagos Digital Works", "Chinedu Okafor", "Nigeria", "Digital services", "A small creative studio helping local traders present their products online.", "Product photography, brand design and catalogue setup", "CO"),
        Business("Akwaaba Cacao", "Abena Mensah", "Ghana", "Food & beverage", "An Accra maker exploring small-batch cocoa gifts with locally sourced ingredients.", "Cocoa gift packs and tasting experiences", "AM")
    )
    val opportunities = listOf(
        Opportunity("Community seed fund", "Funding programme", "Demo: a fictional microenterprise programme to practise preparing a funding application. No funds are available through this listing.", "Example criteria: early-stage entrepreneur, a clear customer problem and a simple spending plan.", "Prepare your founder story, a one-page plan and an itemised startup budget."),
        Opportunity("Women makers grant", "Business grant", "Demo: a fictional grant brief for a women-led craft enterprise. This is not a real grant offer.", "Example criteria: woman-led microbusiness with a sample product and a growth goal.", "Prepare a product description, cost breakdown and a short impact statement."),
        Opportunity("Founder to founder circle", "Mentorship", "Demo: a fictional peer mentoring circle to show how mentorship discovery could work.", "Example criteria: aspiring or established small business owner willing to share lessons.", "Write one challenge you want help with and one skill you can share."),
        Opportunity("Market-ready bootcamp", "Training", "Demo: a fictional workshop series covering pricing, customer discovery and digital storytelling.", "Example criteria: any first-time entrepreneur with an idea to test.", "Bring your business introduction and three questions for prospective customers."),
        Opportunity("Local maker sourcing brief", "Supplier opportunity", "Demo: a fictional retailer looking for locally made gifts. No procurement is taking place.", "Example criteria: makers who can describe materials, production capacity and lead times.", "Prepare a company profile, product catalogue and sample quotation."),
        Opportunity("Neighbourhood catering brief", "Tender", "Demo: a fictional catering tender for learning how to read a procurement brief.", "Example criteria: catering business able to describe capacity and applicable registrations.", "Check the real buyer's official requirements before any future submission. Prepare an example proposal."),
        Opportunity("Next idea showcase", "Business competition", "Demo: a fictional pitching competition for practising a three-minute business pitch.", "Example criteria: a business idea, a target customer and a realistic next milestone.", "Prepare your story, customer problem, proposed solution and a basic budget.")
    )
    val documentTypes = listOf("Business plan", "Company profile", "Business proposal", "Quotation", "Startup budget", "Marketing strategy")
    val languages = listOf("English", "Shona", "isiXhosa", "isiZulu", "Swahili", "French", "Portuguese")
}
