package com.example.inboxtriageagent;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InboxTools {

    @Value("${inbox.path:/workspaces/inbox-triage-agent/inbox}")
    private String inboxPath;

    @Tool(description = "List all unprocessed txt files in the inbox folder")
    public List<String> listFiles() throws IOException {
        return Files.list(Paths.get(inboxPath))
            .filter(p -> p.toString().endsWith(".txt"))
            .map(Path::toString)
            .collect(Collectors.toList());
    }

    @Tool(description = "Read the content of a file given its full path")
    public String readFile(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }

    @Tool(description = "Mark a file with a priority: HIGH, MEDIUM or LOW by renaming it")
    public String markFile(String path, String priority) throws IOException {
        Path source = Paths.get(path);
        String newName = priority + "_" + source.getFileName().toString();
        Path target = source.getParent().resolve(newName);
        Files.move(source, target);
        return "Marked as " + priority + " → " + newName;
    }
}
