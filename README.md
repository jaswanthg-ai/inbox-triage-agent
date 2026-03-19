# Inbox Triage Agent

An intelligent AI-powered agent that autonomously triages inbox files using Large Language Models (LLMs). Built with Spring Boot and Spring AI, this agentic application leverages Groq's fast inference to analyze, prioritize, and organize text files in your inbox directory.

## 🚀 Features

- **Autonomous Operation**: AI agent that works independently to process inbox files
- **Intelligent Triage**: Uses LLM to analyze file content and assign priorities (HIGH, MEDIUM, LOW)
- **Tool-Based Architecture**: Implements AI tools for file system interactions (list, read, rename)
- **Fast Inference**: Powered by Groq's Llama 3.3 70B model for quick processing
- **REST API**: Simple endpoints to trigger triage operations
- **Configurable**: Easily adjust inbox path and AI settings

## 🏗️ Architecture

This agentic application follows a tool-calling pattern where the LLM acts as the reasoning engine:

1. **Perception**: Lists unprocessed .txt files in the inbox directory
2. **Analysis**: Reads file contents and determines priority using LLM reasoning
3. **Action**: Renames files with priority prefixes (HIGH_, MEDIUM_, LOW_)
4. **Feedback**: Provides status updates on triaged files

The agent uses Spring AI's tool annotation system to expose file operations as callable functions to the LLM.

## 🛠️ Tech Stack

- **Java 17** - Modern Java runtime
- **Spring Boot 3.4.5** - Framework for building the application
- **Spring AI** - AI integration framework
- **Groq API** - Fast LLM inference via OpenAI-compatible interface
- **Maven** - Dependency management and build tool

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Groq API key (sign up at [groq.com](https://groq.com))

## 🚀 Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/jaswanthg-ai/inbox-triage-agent.git
   cd inbox-triage-agent
   ```

2. **Configure API Key**
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.ai.openai.api-key=YOUR_GROQ_API_KEY
   ```

3. **Set Inbox Path** (optional)
   ```properties
   inbox.path=/path/to/your/inbox
   ```

4. **Build and Run**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 📖 Usage

### Manual Triage
Place .txt files in your inbox directory and call the triage endpoint:

```bash
curl http://localhost:8080/triage
```

### Agentic Workflow
The AI agent will:
1. Scan for .txt files
2. Read and analyze content
3. Assign priorities based on content analysis
4. Rename files with priority indicators

Example output:
```
Marked as HIGH → HIGH_urgent_meeting.txt
Marked as MEDIUM → MEDIUM_project_update.txt
Marked as LOW → LOW_newsletter.txt
```

## 🔧 Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `spring.ai.openai.api-key` | - | Your Groq API key |
| `spring.ai.openai.base-url` | https://api.groq.com/openai | Groq API endpoint |
| `spring.ai.openai.chat.options.model` | llama-3.3-70b-versatile | LLM model to use |
| `inbox.path` | ./inbox | Path to inbox directory |

## 📡 API Endpoints

- `GET /triage` - Trigger inbox triage operation
- `GET /actuator/health` - Health check endpoint

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
