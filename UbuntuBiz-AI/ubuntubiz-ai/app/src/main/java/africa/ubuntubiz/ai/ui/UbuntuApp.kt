package africa.ubuntubiz.ai.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import africa.ubuntubiz.ai.domain.*

private data class Destination(val route: String,val label: String,val icon: ImageVector)
private val destinations = listOf(Destination("home","Home",Icons.Outlined.Home),Destination("story","My Story",Icons.Outlined.AutoAwesome),Destination("launch","Launch",Icons.Outlined.RocketLaunch),Destination("discover","Discover",Icons.Outlined.Explore),Destination("profile","Profile",Icons.Outlined.PersonOutline))

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun UbuntuApp(vm: UbuntuViewModel = viewModel()) = UbuntuTheme {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route ?: "home"
    fun go(to: String) { nav.navigate(to) { popUpTo(nav.graph.findStartDestination().id) { saveState=true }; launchSingleTop=true; restoreState=true } }
    LaunchedEffect(route) { vm.message=null; vm.error=null }
    Scaffold(
        containerColor=Cream,
        topBar={ TopAppBar(title={ Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(9.dp)) {
            BrandMark(34); Column { Text("UbuntuBiz AI",fontWeight=FontWeight.Bold,fontSize=19.sp); Text("ROOTED IN POSSIBILITY",style=MaterialTheme.typography.labelSmall,color=Muted) }
        } },actions={ IconButton(onClick={go("language")}) { Icon(Icons.Outlined.Translate,"African Language Bridge") }; IconButton(onClick={go("opportunities")}) { Icon(Icons.Outlined.NotificationsNone,"Funding and Opportunities") } },colors=TopAppBarDefaults.topAppBarColors(containerColor=Cream)) },
        bottomBar={ NavigationBar(containerColor=Color.White,tonalElevation=0.dp) { destinations.forEach { d -> NavigationBarItem(selected=route==d.route,onClick={go(d.route)},icon={Icon(d.icon,d.label)},label={Text(d.label,maxLines=1)},colors=NavigationBarItemDefaults.colors(indicatorColor=Pale)) } } }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize(),contentAlignment=Alignment.TopCenter) {
            NavHost(nav,startDestination="home",modifier=Modifier.widthIn(max=760.dp).fillMaxSize()) {
                composable("home") { Home(vm,::go) }
                composable("story") { StoryScreen(vm) }
                composable("launch") { LaunchScreen(vm,::go) }
                composable("discover") { DiscoveryScreen() }
                composable("profile") { ProfileScreen(vm) }
                composable("language") { LanguageScreen { go("home") } }
                composable("opportunities") { OpportunitiesScreen { go("home") } }
            }
        }
    }
}

@Composable private fun BrandMark(size: Int) {
    Box(Modifier.size(size.dp).clip(RoundedCornerShape((size/3).dp)).background(Forest),contentAlignment=Alignment.Center) {
        Text("u",color=Gold,fontWeight=FontWeight.ExtraBold,fontSize=(size*0.82).sp)
    }
}
@Composable private fun Page(resetKey: Any? = null, content: @Composable ColumnScope.() -> Unit) {
    val scroll = rememberScrollState()
    LaunchedEffect(resetKey) { scroll.scrollTo(0) }
    Column(Modifier.fillMaxSize().verticalScroll(scroll).padding(horizontal=22.dp).padding(top=12.dp,bottom=30.dp),verticalArrangement=Arrangement.spacedBy(18.dp),content=content)
}
@Composable private fun Heading(eyebrow: String,title: String,description: String) {
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) { Text(eyebrow.uppercase(),style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.secondary); Text(title,style=MaterialTheme.typography.headlineLarge); Text(description,color=Muted,style=MaterialTheme.typography.bodyLarge) }
}
@Composable private fun Pill(text: String, dark: Boolean=false) {
    Surface(color=if(dark) Color(0xFF2B5548) else Pale,shape=RoundedCornerShape(50)) { Text(text,Modifier.padding(horizontal=12.dp,vertical=7.dp),color=if(dark) Gold else Forest,style=MaterialTheme.typography.labelSmall) }
}
@Composable private fun SectionTitle(text: String,action: String?=null,onAction: ()->Unit={}) {
    Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween) { Text(text,style=MaterialTheme.typography.titleLarge,modifier=Modifier.weight(1f)); if(action!=null) TextButton(onClick=onAction) { Text(action); Icon(Icons.AutoMirrored.Outlined.ArrowForward,null,Modifier.size(16.dp)) } }
}
@Composable private fun Notice(text: String,error: Boolean=false) {
    Surface(color=if(error) MaterialTheme.colorScheme.errorContainer else Pale,shape=RoundedCornerShape(16.dp),modifier=Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp),horizontalArrangement=Arrangement.spacedBy(10.dp)) { Icon(if(error) Icons.Outlined.ErrorOutline else Icons.Outlined.Info,null,Modifier.size(20.dp)); Text(text,style=MaterialTheme.typography.bodyMedium) }
    }
}
@Composable private fun Status(vm: UbuntuViewModel) { vm.error?.let { Notice(it,true) }; vm.message?.let { Notice(it) } }
@Composable private fun ActionCard(icon: ImageVector,title: String,subtitle: String,onClick:()->Unit) {
    Card(onClick=onClick,shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=Color.White),modifier=Modifier.fillMaxWidth()) {
        Row(Modifier.padding(18.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(14.dp)) {
            Box(Modifier.size(48.dp).clip(RoundedCornerShape(15.dp)).background(Pale),contentAlignment=Alignment.Center) { Icon(icon,null,tint=Forest) }
            Column(Modifier.weight(1f)) { Text(title,style=MaterialTheme.typography.titleMedium); Text(subtitle,color=Muted,style=MaterialTheme.typography.bodyMedium) }
            Icon(Icons.AutoMirrored.Outlined.ArrowForward,null,tint=Forest,modifier=Modifier.size(20.dp))
        }
    }
}

/** Vector illustration: a shared market table, rising sun and two makers. */
@Composable private fun MakersIllustration() {
    Canvas(Modifier.fillMaxWidth().height(158.dp)) {
        val sx=size.width/340f; val sy=size.height/160f
        fun rect(c: Color,x:Float,y:Float,w:Float,h:Float) = drawRoundRect(c,Offset(x*sx,y*sy),Size(w*sx,h*sy),androidx.compose.ui.geometry.CornerRadius(10*sx))
        drawCircle(Color(0xFF2D594B),75*sx,Offset(240*sx,80*sy))
        drawCircle(Gold,38*sx,Offset(242*sx,56*sy))
        for(i in 0..4) drawLine(Color(0xFF496E57),Offset((16+i*28)*sx,142*sy),Offset((43+i*28)*sx,112*sy),4*sx,StrokeCap.Round)
        rect(Color(0xFFCF8860),96f,66f,44f,68f); drawCircle(Color(0xFF653D2A),16*sx,Offset(119*sx,48*sy))
        rect(Color(0xFFBCD0B1),198f,62f,42f,71f); drawCircle(Color(0xFF9F6342),16*sx,Offset(219*sx,44*sy))
        drawArc(Color(0xFF26372B),180f,180f,false,Offset(101*sx,28*sy),Size(36*sx,30*sy),style=Stroke(8*sx))
        rect(Gold,73f,116f,200f,10f); rect(Color(0xFFBC9455),85f,126f,8f,32f);rect(Color(0xFFBC9455),252f,126f,8f,32f)
        rect(Color(0xFFDDA37B),146f,93f,26f,23f);rect(Color(0xFFF2DFC0),180f,87f,25f,29f)
        val leaf=Path().apply { moveTo(280*sx,113*sy); quadraticTo(256*sx,78*sy,280*sx,85*sy); quadraticTo(312*sx,68*sy,280*sx,113*sy) };drawPath(leaf,Color(0xFF9DAE78))
        rect(Color(0xFFDBB16B),268f,111f,24f,25f)
    }
}
@Composable private fun Home(vm: UbuntuViewModel,go:(String)->Unit) {
    var selected by remember { mutableStateOf<Business?>(null) }
    Page {
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text("Sawubona. Mhoro. Welcome.",color=Muted,style=MaterialTheme.typography.bodyMedium); Text(if(vm.profile.name.isBlank()) "Your next chapter starts here." else "Welcome, ${vm.profile.name.substringBefore(' ')}.",style=MaterialTheme.typography.titleLarge) }; Pill("OFFLINE DEMO") }
        Card(shape=RoundedCornerShape(28.dp),colors=CardDefaults.cardColors(containerColor=Forest)) {
            Column(Modifier.padding(24.dp),verticalArrangement=Arrangement.spacedBy(13.dp)) {
                Pill("STORIES → POSSIBILITIES",true)
                Text("Your story.\nAfrica’s next\nopportunity.",style=MaterialTheme.typography.headlineLarge,color=Color.White)
                Text("Where African Stories Become African Opportunities.",color=Color(0xFFE3E8DA),style=MaterialTheme.typography.bodyLarge)
                MakersIllustration()
                Button(onClick={go("story")},colors=ButtonDefaults.buttonColors(containerColor=Gold,contentColor=Forest),shape=RoundedCornerShape(14.dp),modifier=Modifier.fillMaxWidth().heightIn(min=52.dp)) { Text("Create My Business Story"); Spacer(Modifier.width(10.dp)); Icon(Icons.Outlined.AutoAwesome,null,Modifier.size(19.dp)) }
            }
        }
        SectionTitle("Build what comes next")
        ActionCard(Icons.Outlined.RocketLaunch,"Launch My Business","Plans, profiles and your first quotation") { go("launch") }
        ActionCard(Icons.Outlined.Public,"Discover African Businesses","Meet makers, growers and creators") { go("discover") }
        ActionCard(Icons.Outlined.WbSunny,"Funding and Opportunities","Explore sample pathways to grow") { go("opportunities") }
        ActionCard(Icons.Outlined.Translate,"Speak across borders","Explore the African Language Bridge") { go("language") }
        SectionTitle("People building possibility","Explore") { go("discover") }
        Text("Featured entrepreneurs • fictional demo profiles",color=Muted,style=MaterialTheme.typography.bodyMedium)
        Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(12.dp)) {
            Catalog.businesses.take(3).forEachIndexed { i,b ->
                Card(onClick={selected=b},shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=Color.White),modifier=Modifier.width(238.dp)) {
                    Box(Modifier.fillMaxWidth().height(110.dp).background(if(i%2==0) Pale else Color(0xFFF0E3CC)),contentAlignment=Alignment.Center) { Avatar(b.initials,60) }
                    Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) { Text(b.country.uppercase(),style=MaterialTheme.typography.labelSmall,color=Muted);Text(b.name,style=MaterialTheme.typography.titleMedium);Text(b.owner,color=Muted);Text(b.category,color=Forest,style=MaterialTheme.typography.bodyMedium) }
                }
            }
        }
        SectionTitle("Recent opportunities","View all") { go("opportunities") }
        Card(shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=Pale)) { Column(Modifier.padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) { Pill("SAMPLE • MENTORSHIP"); Text("Founder to founder circle",style=MaterialTheme.typography.titleMedium);Text("Practise finding the guidance to take your next step.",color=Muted);TextButton(onClick={go("opportunities")}) { Text("Explore demo opportunities");Icon(Icons.AutoMirrored.Outlined.ArrowForward,null) } } }
        Text("I am because we are. Build with your voice, your community and your ambition.",color=Muted,style=MaterialTheme.typography.bodyMedium)
    }
    selected?.let { BusinessDialog(it) { selected=null } }
}
@Composable private fun Field(label: String,value: String,onChange:(String)->Unit,multiline:Boolean=false,numeric:Boolean=false) {
    OutlinedTextField(value=value,onValueChange={onChange(it.take(if(multiline) 12000 else 250))},label={Text(label)},modifier=Modifier.fillMaxWidth(),minLines=if(multiline) 3 else 1,maxLines=if(multiline) 12 else 1,singleLine=!multiline,shape=RoundedCornerShape(14.dp),keyboardOptions=KeyboardOptions(keyboardType=if(numeric) KeyboardType.Decimal else KeyboardType.Text),colors=OutlinedTextFieldDefaults.colors(unfocusedContainerColor=Color.White,focusedContainerColor=Color.White))
}
@Composable private fun ProfileFields(vm: UbuntuViewModel,full: Boolean) {
    val p=vm.profile
    Field("Your name *",p.name,{vm.edit(p.copy(name=it))})
    Field("Business name *",p.business,{vm.edit(p.copy(business=it))})
    Field("Country *",p.country,{vm.edit(p.copy(country=it))})
    Field("Business category *",p.category,{vm.edit(p.copy(category=it))})
    if(full) Field("Skills",p.skills,{vm.edit(p.copy(skills=it))})
    Field("Your personal story *",p.story,{vm.edit(p.copy(story=it))},true)
    Field("Products or services *",p.products,{vm.edit(p.copy(products=it))},true)
    Field("Business goals *",p.goals,{vm.edit(p.copy(goals=it))},true)
}
@Composable private fun StoryScreen(vm: UbuntuViewModel) {
    var results by rememberSaveable { mutableStateOf(false) }
    Page(results) {
        Heading("01 / Your voice, amplified","AI Story Studio","Turn lived experience into a business story. Your words stay at the heart of every draft.")
        Notice("Offline template mode • No external AI is connected. Drafts are saved locally when you transform your story.")
        Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) { FilterChip(selected=!results,onClick={results=false},label={Text("Your story")}); FilterChip(selected=results,onClick={results=true},label={Text("Generated drafts (${vm.story.size})")}) }
        if(!results) {
            Text("Tell it your way",style=MaterialTheme.typography.titleLarge)
            Text("* Required fields. Share only what you are comfortable saving on this device.",color=Muted,style=MaterialTheme.typography.bodyMedium)
            ProfileFields(vm,false)
            Status(vm)
            Button(onClick={vm.transform(); if(vm.error==null) results=true},modifier=Modifier.fillMaxWidth().heightIn(min=54.dp),shape=RoundedCornerShape(16.dp)) { Icon(Icons.Outlined.AutoAwesome,null);Spacer(Modifier.width(10.dp));Text("Transform My Story") }
        } else {
            Status(vm)
            if(vm.story.isEmpty()) EmptyState("Your story belongs here","Complete the form and transform your story to create six useful drafts.") else ContentPreview(vm.story,"UbuntuBiz-story.txt")
        }
    }
}
@Composable private fun EmptyState(title: String,body: String) {
    Card(colors=CardDefaults.cardColors(containerColor=Pale),shape=RoundedCornerShape(22.dp)) { Column(Modifier.padding(28.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) { Icon(Icons.Outlined.Spa,null,tint=Forest,modifier=Modifier.size(36.dp));Text(title,style=MaterialTheme.typography.titleLarge);Text(body,color=Muted) } }
}
@Composable private fun ContentPreview(sections: List<ContentSection>,filename: String) {
    val context=LocalContext.current
    val text=LocalGenerator.plain(sections)
    var exportMessage by remember { mutableStateOf<String?>(null) }
    val currentText by rememberUpdatedState(text)
    val export=rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { uri ->
        if(uri!=null) runCatching { context.contentResolver.openOutputStream(uri)?.use { it.write(currentText.toByteArray(Charsets.UTF_8)) } ?: error("Document could not be opened") }.fold(onSuccess={exportMessage="Text document exported to your selected location."},onFailure={exportMessage="Export failed. Please choose another location and try again."})
    }
    Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
        OutlinedButton(onClick={runCatching { context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type="text/plain";putExtra(Intent.EXTRA_TEXT,text) },"Share reviewed draft")) }.onFailure { exportMessage="No sharing application is available. Try exporting the text." }}) { Icon(Icons.Outlined.Share,null,Modifier.size(18.dp));Spacer(Modifier.width(6.dp));Text("Share") }
        OutlinedButton(onClick={export.launch(filename)}) { Icon(Icons.Outlined.Download,null,Modifier.size(18.dp));Spacer(Modifier.width(6.dp));Text("Export .txt") }
    }
    exportMessage?.let { Notice(it) }
    sections.forEachIndexed { i,s ->
        Card(shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=Color.White),modifier=Modifier.fillMaxWidth()) {
            Column(Modifier.padding(22.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                Text("${(i+1).toString().padStart(2,'0')} / YOUR DRAFT",style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.secondary)
                Text(s.title,style=MaterialTheme.typography.titleLarge)
                HorizontalDivider(color=Pale)
                SelectionContainer { Text(s.body,style=MaterialTheme.typography.bodyLarge) }
            }
        }
    }
}
@Composable private fun LaunchScreen(vm: UbuntuViewModel,go:(String)->Unit) {
    var type by rememberSaveable { mutableStateOf(Catalog.documentTypes.first()) }
    var show by rememberSaveable { mutableStateOf(false) }
    Page(show) {
        Heading("02 / From idea to action","Launch your business","Give your ambition a practical plan. Build a draft, review the details and take the next step.")
        Notice("Local generation templates • These examples are not verified legal, tax or financial advice.")
        Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) { FilterChip(selected=!show,onClick={show=false},label={Text("Create document")}); FilterChip(selected=show,onClick={show=true},label={Text("Saved preview")}) }
        if(show) { Status(vm); if(vm.document.isEmpty()) EmptyState("A blank page, full of potential","Choose a document and generate a draft to see it here.") else ContentPreview(vm.document,"UbuntuBiz-document.txt") }
        else {
            if(vm.profile.business.isBlank() || vm.profile.products.isBlank()) ActionCard(Icons.Outlined.EditNote,"First, introduce your business","Add your founder details in My Story") { go("story") }
            Text("What would you like to create?",style=MaterialTheme.typography.titleLarge)
            Catalog.documentTypes.chunked(2).forEach { row -> Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)) { row.forEach { item ->
                Card(onClick={type=item;vm.error=null;vm.message=null},modifier=Modifier.weight(1f).heightIn(min=100.dp).border(if(type==item) 2.dp else 0.dp,if(type==item) Forest else Color.Transparent,RoundedCornerShape(18.dp)),shape=RoundedCornerShape(18.dp),colors=CardDefaults.cardColors(containerColor=if(type==item) Pale else Color.White)) { Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) { Icon(if(type==item) Icons.Outlined.CheckCircle else Icons.AutoMirrored.Outlined.Article,null,tint=Forest); Text(item,style=MaterialTheme.typography.titleMedium) } }
            } } }
            if(type=="Quotation" || type=="Startup budget") {
                Text("Use your own figures",style=MaterialTheme.typography.titleLarge)
                val m=vm.money
                Field("Currency code (e.g. ZAR)",m.currency,{vm.money=m.copy(currency=it)})
                if(type=="Quotation") { Field("Quantity",m.quantity,{vm.money=m.copy(quantity=it)},numeric=true);Field("Unit price",m.unitPrice,{vm.money=m.copy(unitPrice=it)},numeric=true) }
                else { Field("Equipment estimate",m.equipment,{vm.money=m.copy(equipment=it)},numeric=true);Field("Stock / materials estimate",m.stock,{vm.money=m.copy(stock=it)},numeric=true);Field("Marketing estimate",m.marketing,{vm.money=m.copy(marketing=it)},numeric=true);Field("Working capital reserve",m.reserve,{vm.money=m.copy(reserve=it)},numeric=true) }
            }
            Status(vm)
            Button(onClick={vm.generate(type);if(vm.error==null) show=true},modifier=Modifier.fillMaxWidth().heightIn(min=54.dp),shape=RoundedCornerShape(16.dp)) { Icon(Icons.Outlined.AutoAwesome,null);Spacer(Modifier.width(10.dp));Text("Generate $type") }
            Text("The latest document is saved on this device. Export a copy before generating a replacement.",style=MaterialTheme.typography.bodyMedium,color=Muted)
        }
    }
}
@Composable private fun Avatar(initials:String,size:Int=48) { Box(Modifier.size(size.dp).clip(CircleShape).background(Forest),contentAlignment=Alignment.Center) { Text(initials,color=Gold,fontWeight=FontWeight.Bold,fontSize=(size/3).sp) } }
@Composable private fun BusinessCard(b:Business,onClick:()->Unit) {
    Card(onClick=onClick,shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=Color.White),modifier=Modifier.fillMaxWidth()) { Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) { Avatar(b.initials);Column(Modifier.weight(1f)) { Text(b.name,style=MaterialTheme.typography.titleMedium);Text(b.owner,color=Muted) } }
        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { Pill(b.country);Text(b.category,color=Muted,style=MaterialTheme.typography.bodyMedium,modifier=Modifier.padding(top=4.dp)) }
        Text(b.description,style=MaterialTheme.typography.bodyMedium)
        Text("Offers: ${b.products}",color=Muted,style=MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment=Alignment.CenterVertically) { Text("View business & contact",color=Forest,fontWeight=FontWeight.SemiBold);Spacer(Modifier.width(8.dp));Icon(Icons.AutoMirrored.Outlined.ArrowForward,null,Modifier.size(18.dp)) }
    } }
}
@Composable private fun DiscoveryScreen() {
    var query by rememberSaveable { mutableStateOf("") };var country by rememberSaveable { mutableStateOf("All") };var selected by remember { mutableStateOf<Business?>(null) }
    val matches=Catalog.businesses.filter { (country=="All" || it.country==country) && listOf(it.name,it.owner,it.country,it.category,it.products,it.description).any { field -> field.contains(query.trim(),true) } }
    Page {
        Heading("03 / A continent of possibility","Discover the makers","Find the people creating, growing and building across Africa.")
        OutlinedTextField(query,{query=it},label={Text("Search businesses, skills or countries")},leadingIcon={Icon(Icons.Outlined.Search,null)},trailingIcon={if(query.isNotEmpty()) IconButton(onClick={query=""}) { Icon(Icons.Outlined.Close,"Clear search") }},singleLine=true,shape=RoundedCornerShape(16.dp),modifier=Modifier.fillMaxWidth())
        Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) { (listOf("All")+Catalog.businesses.map { it.country }).forEach { c -> FilterChip(selected=country==c,onClick={country=c},label={Text(c)}) } }
        Text("${matches.size} businesses • fictional demonstration listings",color=Muted,style=MaterialTheme.typography.bodyMedium)
        if(matches.isEmpty()) { EmptyState("No businesses found","Try a product, another country or a broader search.");OutlinedButton(onClick={query="";country="All"}) { Text("Reset search") } }
        matches.forEach { b -> BusinessCard(b) { selected=b } }
    }
    selected?.let { BusinessDialog(it) { selected=null } }
}
@Composable private fun BusinessDialog(b:Business,onDismiss:()->Unit) {
    var contact by remember { mutableStateOf(false) }
    AlertDialog(onDismissRequest=onDismiss,icon={Avatar(b.initials,56)},title={Text(b.name)},text={Column(Modifier.verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) { Text("${b.owner} • ${b.country}\n${b.category}",fontWeight=FontWeight.SemiBold);Text(b.description);Text("Products / services",fontWeight=FontWeight.Bold);Text(b.products);Notice("Fictional demo profile. No real person or business is represented.");if(contact) Notice("Demo contact preview\n\nIn a live directory, this would show contact details shared with the owner's consent. No contact details are available for this fictional listing.") }},confirmButton={TextButton(onClick={contact=true}) { Text(if(contact) "Contact preview shown" else "Contact business") }},dismissButton={TextButton(onClick=onDismiss) { Text("Close") }})
}
@Composable private fun OpportunitiesScreen(back:()->Unit) {
    var filter by rememberSaveable { mutableStateOf("All") };var selected by remember { mutableStateOf<Opportunity?>(null) };var checklist by remember { mutableStateOf(false) }
    Page {
        TextButton(onClick=back) { Icon(Icons.AutoMirrored.Outlined.ArrowBack,null);Spacer(Modifier.width(8.dp));Text("Home") }
        Heading("04 / Open new doors","Opportunities hub","Explore pathways to skills, support and your next customer.")
        Notice("All listings are fictional samples for this demonstration. There are no real funding offers, open tenders or application links here.")
        Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) { (listOf("All")+Catalog.opportunities.map { it.kind }).forEach { kind -> FilterChip(selected=kind==filter,onClick={filter=kind},label={Text(kind)}) } }
        Catalog.opportunities.filter { filter=="All" || filter==it.kind }.forEach { o -> Card(onClick={selected=o;checklist=false},shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=Color.White)) { Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) { Pill("SAMPLE • ${o.kind.uppercase()}");Text(o.title,style=MaterialTheme.typography.titleLarge);Text(o.description,color=Muted);Row { Text("View eligibility & demo action",color=Forest,fontWeight=FontWeight.SemiBold);Spacer(Modifier.width(8.dp));Icon(Icons.AutoMirrored.Outlined.ArrowForward,null,Modifier.size(18.dp)) } } } }
    }
    selected?.let { o -> AlertDialog(onDismissRequest={selected=null},title={Text(o.title)},text={Column(Modifier.verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) { Pill("FICTIONAL OPPORTUNITY");Text(o.description);Text("Example eligibility",fontWeight=FontWeight.Bold);Text(o.eligibility);if(checklist) { Text("Preparation checklist",fontWeight=FontWeight.Bold);Text(o.preparation);Notice("No application has been submitted. This is a learning exercise only.") } }},confirmButton={TextButton(onClick={checklist=true}) { Text(if(checklist) "Checklist displayed" else "Demo: prepare an application") }},dismissButton={TextButton(onClick={selected=null}) { Text("Close") }}) }
}
@Composable private fun LanguageScreen(back:()->Unit) {
    var from by rememberSaveable { mutableStateOf("English") };var to by rememberSaveable { mutableStateOf("Shona") };var text by rememberSaveable { mutableStateOf(Phrasebook.source) };var result by rememberSaveable { mutableStateOf<String?>(null) };var attempted by rememberSaveable { mutableStateOf(false) }
    Page {
        TextButton(onClick=back) { Icon(Icons.AutoMirrored.Outlined.ArrowBack,null);Spacer(Modifier.width(8.dp));Text("Home") }
        Heading("05 / Every voice belongs","African Language Bridge","Explore a welcome across seven languages.")
        Notice("Demo phrasebook, not live translation. Only the sample greeting is supported across languages. Have any translation reviewed by a fluent speaker.")
        Text("Source language",style=MaterialTheme.typography.titleMedium)
        Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) { Catalog.languages.forEach { language -> FilterChip(selected=from==language,onClick={from=language;attempted=false;result=null},label={Text(language)}) } }
        Field("Source text",text,{text=it;attempted=false;result=null},true)
        OutlinedButton(onClick={text=Phrasebook.example(from);attempted=false;result=null}) { Text("Use supported sample greeting") }
        Text("Target language",style=MaterialTheme.typography.titleMedium)
        Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) { Catalog.languages.forEach { language -> FilterChip(selected=to==language,onClick={to=language;attempted=false;result=null},label={Text(language)}) } }
        Button(onClick={attempted=true;result=Phrasebook.translate(text,from,to)},modifier=Modifier.fillMaxWidth().heightIn(min=52.dp)) { Icon(Icons.Outlined.Translate,null);Spacer(Modifier.width(8.dp));Text("Preview demo translation") }
        if(attempted) { if(text.isBlank()) Notice("Enter some source text first.",true) else if(result==null) Notice("This text is not in the offline phrasebook. Use the supported greeting above. A live service is needed for arbitrary text.") else Card(shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=Color.White),modifier=Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) { Pill(if(from==to) "UNCHANGED • SAME LANGUAGE" else "DEMO TRANSLATION • $to"); SelectionContainer { Text(result.orEmpty(),style=MaterialTheme.typography.headlineMedium) }; Text("Original source\n$text",color=Muted) } } }
    }
}
@Composable private fun ProfileScreen(vm:UbuntuViewModel) {
    var confirm by remember { mutableStateOf(false) }
    Page {
        Heading("06 / Rooted in you","Your entrepreneur profile","One place for the story, skills and ambition that make your business yours.")
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(16.dp)) { Avatar(vm.profile.name.split(" ").filter { it.isNotBlank() }.take(2).map { it.first().uppercaseChar() }.joinToString("").ifBlank { "U" },72);Column { Text("Your profile image placeholder",style=MaterialTheme.typography.titleMedium);Text("Your initials, your identity",color=Muted) } }
        Notice("Your profile is private on this device. Edits are stored when you tap Save profile or generate a draft. There is no automatic publishing.")
        ProfileFields(vm,true)
        Status(vm)
        Button(onClick=vm::save,modifier=Modifier.fillMaxWidth().heightIn(min=54.dp)) { Icon(Icons.Outlined.Save,null);Spacer(Modifier.width(8.dp));Text("Save profile") }
        Card(shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=Pale)) { Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) { Text("Your story. Your authorship.",style=MaterialTheme.typography.titleLarge);Text("Original words remain attributed to you. Review and select what to share using the Android share sheet or text export. Only the content you choose leaves this app.");Text("Android device-private storage; not additionally encrypted by this app. Uninstalling removes local data. Keep an exported copy of drafts you need.",color=Muted,style=MaterialTheme.typography.bodyMedium) } }
        OutlinedButton(onClick={confirm=true},colors=ButtonDefaults.outlinedButtonColors(contentColor=MaterialTheme.colorScheme.error)) { Icon(Icons.Outlined.DeleteOutline,null);Spacer(Modifier.width(8.dp));Text("Delete local profile & drafts") }
    }
    if(confirm) AlertDialog(onDismissRequest={confirm=false},title={Text("Delete your local data?")},text={Text("This removes the saved profile, story drafts and latest business document from this device. Exported copies are not removed. This cannot be undone.")},confirmButton={TextButton(onClick={vm.deleteAll();confirm=false}) { Text("Delete local data") }},dismissButton={TextButton(onClick={confirm=false}) { Text("Keep my data") }})
}
