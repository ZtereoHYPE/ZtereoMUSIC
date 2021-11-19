# ZtereoMUSIC
### Is a discord music bot written in java using JDA. The name is prone to be changed in the future.

## How to use:
1. Run `./gradlew build` or your system's equivalent in this repo
2. Move `ZtereoMUSIC-0.0.1-all.jar` in a directory of your choice
3. Create a `config.json5` file with the following structure:
   ```json5
  {
    token: "ODg4MTE5NTk4MDkwNjgyMzc4.YUOD1A.wgB8Na03TEzP0qPeXX3uv5N5eaY", 
    prefix: "!"
  }
    ```
4. Execute with your jvm of choice, requires java 16 for now.

## Roadmap:
### First alpha:
- [ ] Play command with search on YouTube for title.
- [ ] Pause command
- [ ] Skip command
- [ ] Disconnect command 
- [ ] Queue command to view queue
- [ ] VoiceChecks class to check for tunable micro-permissions (eg. if someone in a different channel calls the bot, don't connect)
- [ ] Various listeners if there's nobody in vc for a while or if it gets disconnected or if last disconnects (stop playin)

### Second alpha:
- [ ] Help command (on ping too)
- [ ] Spotify URLs/playlists support (search on YouTube)
- [ ] Queue remove [index] to remove an element
- [ ] Queue clear to clear queue

### Third+ alpha:
- [ ] Toggleable permissions system based on DJ role and based on server
- [ ] Ability to search the song name
- [ ] Fancy embeds for everything!
- [ ] Slash commands? 
- [ ] Proper database for server preferences (prefix etc)
- Other TBA...

## Thankies
This wouldn't have been possible without the amazing folks behind [lavaplayer](https://github.com/sedmelluq/lavaplayer) and [JDA](https://github.com/DV8FromTheWorld/JDA).
And without ReperakDev and FoxShadew. Thank you.