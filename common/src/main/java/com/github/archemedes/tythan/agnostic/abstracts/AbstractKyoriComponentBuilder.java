package com.github.archemedes.tythan.agnostic.abstracts;

import com.github.archemedes.tythan.Tythan;
import com.github.archemedes.tythan.agnostic.Sender;
import com.github.archemedes.tythan.utils.TColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"unused","rawtype"})
public abstract class AbstractKyoriComponentBuilder<E extends AbstractKyoriComponentBuilder<E>>  {
    protected final TextComponent.Builder handle;
    protected AbstractKyoriComponentBuilder(String initialContent){
        this.handle = Component.text(initialContent).toBuilder();
    }

    public MiniMessage minimessage(){return MiniMessage.miniMessage();}
    public E appendRich(@NotNull String mini_message) {return append(this.minimessage().deserialize(mini_message));}
    public E appendRich(@NotNull String mini_message, TagResolver tag) {return append(this.minimessage().deserialize(mini_message,tag));}
    public E appendRich(@NotNull String mini_message, TagResolver... tag) {return append(this.minimessage().deserialize(mini_message,tag));}

    public E appendRichWithSpace(@NotNull String mini_message) {return append(this.minimessage().deserialize(mini_message)).space();}
    public E appendRichWithSpace(@NotNull String mini_message, TagResolver tag) {return append(this.minimessage().deserialize(mini_message,tag)).space();}
    public E appendRichWithSpace(@NotNull String mini_message, TagResolver... tag) {return append(this.minimessage().deserialize(mini_message,tag)).space();}

    public E appendRichWithNewline(@NotNull String mini_message) {return append(this.minimessage().deserialize(mini_message)).newline();}
    public E appendRichWithNewline(@NotNull String mini_message, TagResolver tag) {return append(this.minimessage().deserialize(mini_message,tag)).newline();}
    public E appendRichWithNewline(@NotNull String mini_message, TagResolver... tag) {return append(this.minimessage().deserialize(mini_message,tag)).newline();}

    public E appendJoined(@NotNull JoinConfiguration join, @NotNull List<Component> components) {return append(Component.join(join,components));}
    public E appendJoined(@NotNull Component join, @NotNull List<Component> components) {return append(Component.join(JoinConfiguration.separator(join),components));}
    public E appendJoined(@NotNull String join, @NotNull List<Component> components) {return append(Component.join(JoinConfiguration.separator(Component.text(join).asComponent()),components));}
    public E appendJoinedWithNewline(@NotNull List<Component> components) {return append(Component.join(JoinConfiguration.newlines(),components));}

    public E appendBracketed(char ch){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", TColor.GRAY).append(ch, TColor.GRAY).append("]", TColor.GRAY);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(char ch, @NotNull TextColor bracketColor){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", bracketColor).append(ch, bracketColor).append("]", bracketColor);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(char ch, @NotNull TextColor bracketColor, @NotNull TextColor characterColor){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", bracketColor).append(ch, characterColor).append("]", bracketColor);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(char ch, @NotNull TextColor bracketColor, @NotNull TextColor characterColor, @NotNull TextDecoration decoration){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", bracketColor, decoration).append(ch, characterColor).append("]", bracketColor, decoration);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(@NotNull String word){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", TColor.GRAY).append(word, TColor.GRAY).append("]", TColor.GRAY);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(@NotNull String word, @NotNull TextColor bracketColor){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", bracketColor).append(word, bracketColor).append("]", bracketColor);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(@NotNull String word, @NotNull TextColor bracketColor, @NotNull TextColor wordColor){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", bracketColor).append(word, wordColor).append("]", bracketColor);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(@NotNull String word, @NotNull TextColor bracketColor, @NotNull TextColor wordColor, @NotNull TextDecoration decoration){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", bracketColor, decoration).append(word, wordColor).append("]", bracketColor, decoration);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(@NotNull Component component){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", TColor.GRAY).append(component).append("]", TColor.GRAY);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(@NotNull Component component, @NotNull TextColor bracketColor){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", bracketColor).append(component).append("]", bracketColor);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(@NotNull Component component, @NotNull TextColor bracketColor, @NotNull TextColor wordColor, @NotNull TextDecoration decoration){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", bracketColor, decoration).append(component.color(wordColor)).append("]", bracketColor, decoration);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(@NotNull Number number){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", TColor.GRAY).append(number, TColor.GRAY).append("]", TColor.GRAY);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(@NotNull Number number, @NotNull TextColor bracketColor){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", bracketColor).append(number, bracketColor).append("]", bracketColor);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(@NotNull Number number, @NotNull TextColor bracketColor, @NotNull TextColor numberColor){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", bracketColor).append(number, numberColor).append("]", bracketColor);
        handle.append(subComponent.build());
        return getThis();
    }

    public E appendBracketed(@NotNull Number number, @NotNull TextColor bracketColor,@NotNull TextColor numberColor, @NotNull TextDecoration decoration){
        AbstractKyoriComponentBuilder subComponent = Tythan.get().getKyoriComponentBuilder().append("[", bracketColor, decoration).append(number, numberColor).append("]", bracketColor, decoration);
        handle.append(subComponent.build());
        return getThis();
    }



    public E appendWithSpace(char ch) {
        handle.append(Component.text(String.valueOf(ch))).append(Component.space());
        return getThis();
    }

    public E appendWithSpace(char ch, @NotNull TextColor color) {
        handle.append(Component.text(String.valueOf(ch), color)).append(Component.space());
        return getThis();
    }

    public E appendWithSpace(char ch, @NotNull TextColor color, @NotNull TextDecoration decoration) {
        handle.append(Component.text(String.valueOf(ch), color, decoration)).append(Component.space());
        return getThis();
    }

    public E appendWithNewline(char ch) {
        handle.append(Component.text(String.valueOf(ch))).append(Component.newline());
        return getThis();
    }

    public E appendWithNewline(char ch, @NotNull TextColor color) {
        handle.append(Component.text(String.valueOf(ch), color)).append(Component.newline());
        return getThis();
    }

    public E appendWithNewline(char ch, @NotNull TextColor color, @NotNull TextDecoration decoration) {
        handle.append(Component.text(String.valueOf(ch), color, decoration)).append(Component.newline());
        return getThis();
    }
    public E append(char ch) {
        handle.append(Component.text(String.valueOf(ch)));
        return getThis();
    }

    public E append(char ch, @NotNull TextColor color) {
        handle.append(Component.text(String.valueOf(ch), color));
        return getThis();
    }

    public E append(char ch, @NotNull TextColor color, @NotNull TextDecoration decoration) {
        handle.append(Component.text(String.valueOf(ch), color, decoration));
        return getThis();
    }

    public E appendWithNewline(@NotNull Number number) {
        handle.append(Component.text(String.valueOf(number))).append(Component.newline());
        return getThis();
    }

    public E appendWithNewline(@NotNull Number number, @NotNull TextColor color) {
        handle.append(Component.text(String.valueOf(number), color)).append(Component.newline());
        return getThis();
    }

    public E appendWithNewline(Number number, @NotNull TextColor color, @NotNull TextDecoration decoration) {
        handle.append(Component.text(String.valueOf(number), color, decoration)).append(Component.newline());
        return getThis();
    }

    public E appendWithSpace(@NotNull Number number) {
        handle.append(Component.text(String.valueOf(number))).append(Component.space());
        return getThis();
    }

    public E appendWithSpace(@NotNull Number number, @NotNull TextColor color) {
        handle.append(Component.text(String.valueOf(number), color)).append(Component.space());
        return getThis();
    }

    public E appendWithSpace(@NotNull Number number, @NotNull TextColor color, @NotNull TextDecoration decoration) {
        handle.append(Component.text(String.valueOf(number), color, decoration)).append(Component.space());
        return getThis();
    }

    public E append(@NotNull Number number) {
        handle.append(Component.text(String.valueOf(number)));
        return getThis();
    }

    public E append(@NotNull Number number, @NotNull TextColor color) {
        handle.append(Component.text(String.valueOf(number), color));
        return getThis();
    }

    public E append(Number number, TextColor color, TextDecoration decoration) {
        handle.append(Component.text(String.valueOf(number), color, decoration));
        return getThis();
    }

    public E appendWithNewline(@NotNull String string) {
        handle.append(Component.text(String.valueOf(string))).append(Component.newline());
        return getThis();
    }

    public E appendWithNewline(@NotNull String string, @NotNull TextColor color) {
        handle.append(Component.text(String.valueOf(string), color)).append(Component.newline());
        return getThis();
    }

    public E appendWithNewline(@NotNull String string, @NotNull TextColor color, @NotNull TextDecoration decoration) {
        handle.append(Component.text(String.valueOf(string), color, decoration)).append(Component.newline());
        return getThis();
    }

    public E appendWithNewline(@NotNull Component component) {
        handle.append(component).append(Component.newline());
        return getThis();
    }

    public E appendWithNewline(@NotNull List<Component> components) {
        handle.append(components).append(Component.newline());
        return getThis();
    }

    public E appendWithNewline(@NotNull AbstractKyoriComponentBuilder acb) {
        handle.append(acb.newline().build());
        return getThis();
    }

    public E appendWithSpace(@NotNull String string) {
        handle.append(Component.text(String.valueOf(string))).append(Component.space());
        return getThis();
    }

    public E appendWithSpace(@NotNull String string, @NotNull TextColor color) {
        handle.append(Component.text(String.valueOf(string), color)).append(Component.space());
        return getThis();
    }

    public E appendWithSpace(@NotNull String string, @NotNull TextColor color, @NotNull TextDecoration decoration) {
        handle.append(Component.text(String.valueOf(string), color, decoration)).append(Component.space());
        return getThis();
    }

    public E appendWithSpace(@NotNull Component component) {
        handle.append(component).append(Component.space());
        return getThis();
    }

    public E appendWithSpace(@NotNull List<Component> components) {
        handle.append(components).append(Component.space());
        return getThis();
    }

    public E appendWithSpace(@NotNull AbstractKyoriComponentBuilder acb) {
        handle.append(acb.space().build());
        return getThis();
    }

    public E append(@NotNull String string) {
        handle.append(Component.text(String.valueOf(string)));
        return getThis();
    }

    public E append(@NotNull String string, @NotNull TextColor color) {
        handle.append(Component.text(String.valueOf(string), color));
        return getThis();
    }

    public E append(@NotNull String string, @NotNull TextColor color, @NotNull TextDecoration decoration) {
        handle.append(Component.text(String.valueOf(string), color, decoration));
        return getThis();
    }

    public E append(@NotNull Component component) {
        handle.append(component);
        return getThis();
    }

    public E append(@NotNull List<Component> components) {
        handle.append(components);
        return getThis();
    }

    public E append(@NotNull AbstractKyoriComponentBuilder acb) {
        handle.append(acb.build());
        return getThis();
    }

    public E newline(){
        return getThis().append(Component.newline());
    }

    public E decorate(@NotNull TextDecoration textDecoration) {
        handle.decorate(textDecoration);
        return getThis();
    }

    public E bold(){
        handle.decorate(TextDecoration.BOLD);
        return getThis();
    }

    public E italic(){
        handle.decorate(TextDecoration.ITALIC);
        return getThis();
    }

    public E strike(){
        handle.decorate(TextDecoration.STRIKETHROUGH);
        return getThis();
    }

    public E magic(){
        handle.decorate(TextDecoration.OBFUSCATED);
        return getThis();
    }

    public E underline(){
        handle.decorate(TextDecoration.UNDERLINED);
        return getThis();
    }

    public E color(@NotNull TextColor color){
        handle.color(color);
        return getThis();
    }

    public E hoverText(@NotNull Component component){
        return hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, component));
    }

    public E hoverText(@NotNull AbstractKyoriComponentBuilder component){
        return hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, component.build()));
    }

    public E hoverText(@NotNull String content){
        return hoverText(Component.text(content));
    }

    public E hoverRichText(@NotNull String mini_message) {return hoverText(this.minimessage().deserialize(mini_message));}
    public E hoverRichText(@NotNull String mini_message,@NotNull TagResolver tag) {return hoverText(this.minimessage().deserialize(mini_message,tag));}
    public E hoverRichText(@NotNull String mini_message,@NotNull TagResolver... tags) {return hoverText(this.minimessage().deserialize(mini_message,tags));}

    public E onClickCopyToClipboard(@NotNull String content){
        return clickEvent(ClickEvent.copyToClipboard(content));
    }

    public E onClickRunCommand(@NotNull String command){
        return clickEvent(ClickEvent.runCommand(command));
    }

    public E onClickChangePage(int pageToGoTo){
        return clickEvent(ClickEvent.changePage(pageToGoTo));
    }

    public E onClickSuggestCommand(@NotNull String suggestion){
        return clickEvent(ClickEvent.suggestCommand(suggestion));
    }

    public E onClickOpenUrl(@NotNull String url){
        return clickEvent(ClickEvent.openUrl(url));
    }

    public E onClickOpenFileOnClient(@NotNull String file){
        return clickEvent(ClickEvent.openFile(file));
    }

    public E appendButton(@NotNull String content, String command, String hoverText, TextColor textColor, TextColor bracketColor, TextColor hoverTextColor, TextDecoration hoverTextDecoration){
        handle.append(buttonComponent(content, command, hoverText, textColor, bracketColor, hoverTextColor, hoverTextDecoration));
        return getThis();
    }

    public E appendButton(@NotNull String content, @NotNull String command){
        handle.append(buttonComponent(content, command, null, TColor.GRAY, TColor.BLUE, null, null));
        return getThis();
    }
    public E appendButton(@NotNull String content, @NotNull String command, @NotNull String hoverText){
        handle.append(buttonComponent(content, command, hoverText, TColor.GRAY, TColor.BLUE, TColor.GRAY, TextDecoration.ITALIC));
        return getThis();
    }

    public E appendButton(@NotNull String content, @NotNull String command, @NotNull TextColor textColor, @NotNull TextColor bracketColor){
        handle.append(buttonComponent(content, command, null, textColor, bracketColor,null,null));
        return getThis();
    }

    public E appendButton(@NotNull String content, @NotNull String command, @NotNull Component hoverText, @NotNull TextColor textColor, @NotNull TextColor bracketColor){
        handle.append(buttonComponent(content, command, hoverText, textColor, bracketColor));
        return getThis();
    }

    public E appendButton(@NotNull String content, @NotNull String command, @NotNull String hoverText, @NotNull TextColor textColor, @NotNull TextColor bracketColor){
        handle.append(buttonComponent(content, command, hoverText, textColor, bracketColor,TColor.GRAY,TextDecoration.ITALIC));
        return getThis();
    }


    private static Component buttonComponent(@NotNull String text, @NotNull String command, @NotNull String hoverText, @NotNull TextColor textColor, @NotNull TextColor bracketColor, @NotNull TextColor hoverTextColor, @NotNull TextDecoration hoverTextDecoration){
        AbstractKyoriComponentBuilder buttonTextComponent = Tythan.get().getKyoriComponentBuilder().appendBracketed(text, textColor, bracketColor);
        buttonTextComponent.onClickRunCommand(command);
        if(hoverText != null){
            if (!hoverText.isEmpty()) {
                AbstractKyoriComponentBuilder hoverComponent = Tythan.get().getKyoriComponentBuilder();
                hoverComponent.append(hoverText,hoverTextColor,hoverTextDecoration);
                buttonTextComponent.hoverText(hoverComponent.build());
            }
        }
        return buttonTextComponent.build();
    }

    private static Component buttonComponent(@NotNull String text, @NotNull String command, @Nullable Component hoverText, @NotNull TextColor textColor, @NotNull TextColor bracketColor){
        AbstractKyoriComponentBuilder buttonTextComponent = Tythan.get().getKyoriComponentBuilder().appendBracketed(text, textColor, bracketColor);
        buttonTextComponent.onClickRunCommand(command);
        if(hoverText != null){
            AbstractKyoriComponentBuilder hoverComponent = Tythan.get().getKyoriComponentBuilder();
            hoverComponent.append(hoverText);
            buttonTextComponent.hoverText(hoverComponent.build());
        }
        return buttonTextComponent.build();
    }

    public E clickEvent(@NotNull ClickEvent clickEvent){
        handle.clickEvent(clickEvent);
        return getThis();
    }

    public E hoverEvent(@NotNull HoverEvent event){
        handle.hoverEvent(event);
        return getThis();
    }

    public E send(@NotNull Sender sender) {
        sender.sendMessage(handle.build());
        return getThis();
    }

    public E space() {
        handle.append(Component.space());
        return getThis();
    }

    public E appendFromGSON(@NotNull String gson) {
        handle.append(GsonComponentSerializer.gson().deserialize(gson));
        return getThis();
    }

    @NotNull public Component build(){
        return handle.build();
    }

    @NotNull public String toGson() {
        return GsonComponentSerializer.gson().serialize(this.build());
    }

    @NotNull public String toSerializedMiniMessage() {
        return MiniMessage.miniMessage().serialize(this.build());
    }
    protected abstract E getThis();


}
