import { test } from "node:test";
import assert from "node:assert/strict";
import { inputSchema,localGenerate } from "../lib/generation.ts";
const p=inputSchema.parse({kind:"Story kit",name:"Rudo",business:"Rudo Herbs",country:"Zimbabwe",category:"Agriculture",story:"I learned to grow herbs in our family garden.",products:"Herbs and seedlings",goals:"Supply three kitchens"});
test("verbatim testimony and attribution",()=>{const d=localGenerate(p);assert.ok(d.sections.some(s=>s.body.includes(`“${p.story}”`)&&s.body.includes("Story by Rudo")));assert.ok(d.reviewNote.includes("not verified"));});
test("required input is validated",()=>{assert.equal(inputSchema.safeParse({...p,name:""}).success,false);assert.equal(inputSchema.safeParse({...p,story:"Too short"}).success,false);});
test("every kind yields reviewable sections",()=>{for(const kind of ["Story kit","Business plan","Company profile","Business proposal","Marketing strategy","Business mentor"] as const){assert.ok(localGenerate({...p,kind}).sections.length>=5);}});
