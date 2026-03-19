# Inbox Triage Agent

An agentic AI system built in Java that autonomously monitors an inbox folder, reads unstructured text files, and classifies each as HIGH, MEDIUM, or LOW priority — using LLM reasoning with zero hardcoded rules.

Built to understand the foundation of agentic systems: how LLMs use tools to interact with the real world.

## The problem it solves

Traditional inbox automation relies on keyword matching or rule engines:
- "If subject contains URGENT → mark HIGH"
- Brittle. Misses context. Breaks constantly.

This agent reads the actual content and understands intent — the same way a human would. A message about a production outage is HIGH not because it contains a keyword, but because the LLM understands what a production outage means.

## How it works
```
User: "Triage my inbox"
         ↓
   LLM (Llama 3.2 via Groq)
         ↓ ReAct loop — think, act, observe, repeat
   ┌─────────────────────────────────────┐
   │  Tool 1: listFiles()                │ → scans inbox, returns file paths
   │  Tool 2: readFile(path)             │ → reads raw file content
   │  Tool 3: markFile(path, priority)   │ → renames file with priority label
   └─────────────────────────────────────┘
         ↓
   Final summary with reasoning per file
```

The LLM decides which tools to call, in what order, and what priority to assign — based purely on understanding the content. No orchestration logic written.

## Architecture decisions and tradeoffs

**Why Spring AI over LangChain4j?**
Spring AI 1.0 has first-class tool calling support with `@Tool` annotation. Fits naturally into existing Spring Boot microservice patterns. LangChain4j is good but adds complexity for simple tool calling use cases.

**Why Groq over OpenAI?**
Groq's LPU hardware runs Llama 3.2 at ~800 tokens/second vs ~50 on GPU. For a triage agent processing many files, inference speed directly impacts throughput. Cost is also zero at current free tier.

**Why three separate tools instead of one?**
Single responsibility. `listFiles` has no business reading content. `readFile` has no business making priority decisions. Separation makes each tool testable independently and lets the LLM compose them as needed. If reading fails, listing still works.

**The security boundary**
Tools are the only way the LLM can interact with the filesystem. It cannot list, read, or rename anything that isn't explicitly exposed as a `@Tool`. The LLM has no direct filesystem access — Java code does, and Java code is the gatekeeper.

## What this demonstrates

- **Tool calling pattern** — how LLMs interact with real systems through structured function calls
- **ReAct loop** — the think-act-observe cycle that makes an LLM an agent rather than a chatbot
- **Tool design** — single responsibility, clear descriptions, controlled scope
- **Agentic security model** — LLM capabilities are bounded by the tools you expose, nothing more

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3.4 |
| AI integration | Spring AI 1.0 |
| LLM | Llama 3.2 via Groq API |
| Build | Maven |

## Run it locally
```bash
# Create inbox and test files
mkdir C:\inbox
echo "Production server is down. Users cannot login. Payments failing." > C:\inbox\alert1.txt
echo "Please update the team wiki page when you get a chance." > C:\inbox\task1.txt
echo "Team lunch is moved to Friday instead of Thursday." > C:\inbox\info1.txt
```
```properties
# application.properties
spring.ai.openai.api-key=YOUR_GROQ_KEY
spring.ai.openai.base-url=https://api.groq.com/openai
spring.ai.openai.chat.options.model=llama-3.2-11b-text-preview
```
```bash
mvn spring-boot:run
```

## Sample output
```
AGENT: I've triaged all 3 files in your inbox:

HIGH_alert1.txt   — Production server down with payment failures. 
                    Requires immediate attention.

MEDIUM_task1.txt  — Wiki update request. Important but not time-critical. 
                    Can be handled today or tomorrow.

LOW_info1.txt     — Schedule change notification. 
                    Informational only, no action required.
```

## What's next

- Add email ingestion tool — fetch unread emails, triage them the same way
- Add notification tool — send Slack message for HIGH priority items automatically  
- Move tools to a separate MCP server — any agent can then use the same inbox tools
- Add human-in-the-loop — HIGH priority items require human confirmation before marking
