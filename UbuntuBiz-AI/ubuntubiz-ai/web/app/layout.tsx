import type { Metadata } from "next";
import "./globals.css";
export const metadata:Metadata={title:"UbuntuBiz AI • Your story, your opportunity",description:"Where African Stories Become African Opportunities."};
export default function Layout({children}:{children:React.ReactNode}){return <html lang="en"><body>{children}</body></html>;}
