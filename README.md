# ZtereoMUSIC
#### A discord music bot written in java using JDA. The name is prone to be changed in the future.

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

## To Dos:
- Play command
  - search on youtube for title and spotify songs.
- Pause command
- Skip command
- Disconnect command
- Help command
- Queue commands:
  - Queue to view the queue
  - Queue remove [index] to remove an element
  - Queue clear to clear queue
- Toggleable permissions system based on DJ role and based on server
- Ability to search the song name
- Spotify URLs/playlists support
- Fancy embeds for everything!
- Slash commands? idk
- Proper database for server preferences (prefix etc)
- Various listeners if there's nobody in vc for a while or if it gets disconnected or if last disconnects (stop playin)
- Other TBA...

## Thankies
This wouldn't have been possible without the amazing folks behind [lavaplayer](https://github.com/sedmelluq/lavaplayer) and [JDA](https://github.com/DV8FromTheWorld/JDA).
And without @ReperakDev and @FoxShadew. Thank you.
