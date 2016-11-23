# MtOlympus
CS3733 Individual Project written by Shao Zhou (szhou2@wpi.edu).

I've included `ks_lib.jar` and the card images as dependencies. You can also find the necessary project setup file in for IntelliJ. Sorry I've no experience packaging projects for Eclipse.

# Rules
* I decided to disable undoing dealing of cards.
* If a move results in an empty column and there are still cards in the deck, a new card will be dealt to fill the empty column. Undoing this move will return the dealt card to the deck.
* A valid column is defined as a column of cards of the same suit, with common difference of two, increasing from the top card of the column (top defined as the last added card to the column)
* If a move results in an empty column and there are no cards in the deck, a valid column can be dragged onto the empty column
* You can drag an entire valid column to a pile in the foundation. As long as the suit and rank difference match, the pile will take the entire column.

__93% code coverage as determined by IntelliJ's coverage plugin.__
