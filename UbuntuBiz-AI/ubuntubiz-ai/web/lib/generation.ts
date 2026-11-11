import { z } from "zod";
export const inputSchema=z.object({
 kind:z.enum(["Story kit","Business plan","Company profile","Business proposal","Marketing strategy","Business mentor"]),
 name:z.string().trim().min(1).max(120),business:z.string().trim().min(1).max(120),country:z.string().trim().min(1).max(100),category:z.string().trim().min(1).max(120),story:z.string().trim().min(20).max(12000),products:z.string().trim().min(1).max(4000),goals:z.string().trim().min(1).max(4000)
});
export type FounderInput=z.infer<typeof inputSchema>;
export const documentSchema=z.object({title:z.string(),sections:z.array(z.object({title:z.string(),body:z.string()})).min(1),reviewNote:z.string()});
export const outputJsonSchema={type:"object",additionalProperties:false,properties:{title:{type:"string"},sections:{type:"array",items:{type:"object",additionalProperties:false,properties:{title:{type:"string"},body:{type:"string"}},required:["title","body"]}},reviewNote:{type:"string"}},required:["title","sections","reviewNote"]};
export function localGenerate(p:FounderInput){
 const intro={title:"Business introduction",body:`My name is ${p.name}. I am building ${p.business} in ${p.country}, in ${p.category}. We offer ${p.products}.`};
 const original={title:"Original testimony",body:`“${p.story}”\n\nStory by ${p.name}.`};
 const goal={title:"Your stated goal",body:p.goals};
 const sections=p.kind==="Story kit"?[intro,original,goal,
 {title:"Short biography",body:`${p.name}, founder of ${p.business}, is an entrepreneur in ${p.country}. Offering: ${p.products}.`},
 {title:"Social media caption",body:`Meet ${p.business}. Here is our beginning: “${p.story}”\n#UbuntuBiz #AfricanEntrepreneurs`},
 {title:"Product description",body:`${p.products}\nAsk ${p.business} about specifications, availability and pricing.`},
 {title:"Marketing message",body:`Discover ${p.business} in ${p.country}. ${p.products}\nConnect with ${p.name} to find out more.`}]:[intro,original,goal,
 {title:p.kind==="Business mentor"?"Your next practical steps":"Working plan to adapt",body:"1. Choose one customer group and interview five potential customers.\n2. Price one offering using actual costs and your time.\n3. Test a small paid order before investing further.\n4. Review feedback, expenses and repeat demand."},
 {title:"Details to verify",body:"Confirm customer needs, delivery scope, supplier prices, taxes and local requirements. Add client names, evidence, timelines and agreed commercial terms before using this draft."}];
 return documentSchema.parse({title:`${p.kind} • ${p.business}`,sections,reviewNote:"Offline template draft, not verified legal or financial advice. Original testimony remains attributed to its author. Nothing is automatically published."});
}
