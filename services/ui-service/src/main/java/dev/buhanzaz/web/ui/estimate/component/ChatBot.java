package dev.buhanzaz.web.ui.estimate.component;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import dev.buhanzaz.web.dto.chat.ChatRequestDto;
import dev.buhanzaz.web.dto.chat.ChatResponseDto;
import dev.buhanzaz.web.client.LLMClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.UUID;


@UIScope
@SpringComponent
public class ChatBot extends Composite<VerticalLayout> {
    private final transient LLMClient llmClient;
    private final MessageList messageList;
    private final Scroller scroller;
    private final String chatId = UUID.randomUUID().toString();

    public ChatBot(@Autowired LLMClient llmClient) {
        this.llmClient = llmClient;

        VerticalLayout root = getContent();
        root.setSizeFull();
        root.setPadding(false);
        root.setSpacing(false);
        root.setMargin(false);
        root.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);

        // КЛЮЧЕВОЕ: запрещаем контейнеру расти выше отведённого места
        root.getStyle().set("overflow", "hidden");
        root.getStyle().set("min-height", "0");

        // область сообщений
        messageList = new MessageList();
        scroller = new Scroller(messageList);
        scroller.setWidthFull();
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        // scroller должен заполнять оставшееся место, но не раздувать родителя
        scroller.getStyle().set("flex", "1 1 0");
        scroller.getStyle().set("min-height", "0");
        scroller.getStyle().set("overflow-y", "auto");

        // поле ввода — фиксированная высота, не сжимается
        MessageInput messageInput = new MessageInput();
        messageInput.addSubmitListener(this::onSubmit);
        messageInput.setWidthFull();
        messageInput.getStyle().set("flex-shrink", "0");

        root.add(scroller, messageInput);
    }

    private void onSubmit(MessageInput.SubmitEvent submitEvent) {
        //create and handle a prompt message
        var promptMessage = new MessageListItem(submitEvent.getValue(), Instant.now(), "User");
        promptMessage.setUserColorIndex(0);
        messageList.addItem(promptMessage);

        //create and handle the response message
        var responseMessage = new MessageListItem("", Instant.now(), "Bot");
        responseMessage.setUserColorIndex(1);
        messageList.addItem(responseMessage);

        // прокручиваем вниз после добавления сообщения пользователя (с задержкой для обновления DOM)
        scrollToBottom();

        //append a response message to the existing UI
        var userPrompt = submitEvent.getValue();

        ChatResponseDto chatResponseDto = llmClient.generateEstimate(
                new ChatRequestDto(
                        this.chatId,
                        userPrompt
                )
        );

        if (chatResponseDto == null) throw new RuntimeException("No response from LLM");

        switch (chatResponseDto.status()) {
            case NEED_CLARIFICATION -> {
                var helpMessage = new MessageListItem("", Instant.now(), "Helper");
                StringBuilder sb = new StringBuilder();
                chatResponseDto.options().forEach(value -> sb.append(value).append(",\n"));
                helpMessage.appendText(sb.toString());
                helpMessage.setUserColorIndex(2);
                messageList.addItem(helpMessage);
            }
            case FINAL -> responseMessage.appendText(chatResponseDto.fileUrl());
            default -> throw new RuntimeException("No response from LLM");
        }
        responseMessage.appendText(chatResponseDto.message());

        // прокручиваем вниз после получения ответа
        scrollToBottom();
    }

    private void scrollToBottom() {
        // Используем JavaScript для гарантированной прокрутки после обновления DOM
        // Прокручиваем и scroller, и внутренний messageList
        scroller.getElement().executeJs(
                "setTimeout(() => { this.scrollTop = this.scrollHeight; }, 100);"
        );
        messageList.getElement().executeJs(
                "setTimeout(() => { this.scrollIntoView({block: 'end', behavior: 'smooth'}); }, 100);"
        );
    }
}
