import pg from "pg";
import { readFile } from "node:fs/promises";
if(!process.env.DATABASE_URL)throw Error("Set DATABASE_URL first.");
const client=new pg.Client({connectionString:process.env.DATABASE_URL,ssl:process.env.DATABASE_SSL==="true"?{rejectUnauthorized:true}:undefined});
await client.connect();try{await client.query(await readFile(new URL("../../database/001_initial.sql",import.meta.url),"utf8"));console.log("Schema migration completed.");}finally{await client.end();}
