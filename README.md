# ZtereoMUSIC
### Is a discord music bot written in java using JDA. The name is prone to be changed in the future.

## How to use:
1. Run `./gradlew build` or your system's equivalent in this repo
2. Move `ZtereoMUSIC-0.0.1-all.jar` in a directory of your choice
3. Create a `config.json5` file with the following structure:
   ```json5
    {
        token: "ODg4MTE5NTk4MDkwNjgyMzc4.YUOD1A.wgB8Na03TEzP0qPeXX3uv5N5eaY", 
        prefix: "!",
        yt_api_key: "djasfhjasdhHOUIHIDhdi_oHIUShPIUAHSF",
    }
    ```
4. Execute with your jvm of choice, requires java 16 for now.

## Roadmap:
### First alpha:
- [x] Play command with search on YouTube for title.
- [x] Pause command
- [x] Skip command
- [x] Disconnect command
- [x] Queue command to view queue (half assed lol)
- [ ] Remove [index] to remove an element
- [ ] Clear to clear queue
- [ ] Clean up todos and code!

### Second alpha:
- [ ] Fancy embeds for everything!
- [x] VoiceChecks class to check for tunable micro-permissions (eg. if someone in a different channel calls the bot, don't connect)
- [ ] Various listeners if there's nobody in vc for a while or if it gets disconnected or if last disconnects (stop playin) (very important as cleanup doesnt always get called)
- [ ] Help command (on ping too)
- [ ] Spotify URLs/playlists support (search on YouTube)
- [ ] When a track fails try to play it again and if that fails send the error message.

### Later:
- [ ] Toggleable permissions system based on DJ role and based on server
- [ ] fast forward command
- [ ] Ability to search the song name
- [ ] Slash commands? 
- [ ] Proper database for server preferences (prefix etc)
- Other TBA...

## Thankies
This wouldn't have been possible without the amazing folks behind [lavaplayer](https://github.com/sedmelluq/lavaplayer) and [JDA](https://github.com/DV8FromTheWorld/JDA).
And without ReperakDev and FoxShadew. Thank you.
