If you want to translate messages, you can edit original lang_en_GB.yml file
or you can create copy of this file and edit that copy, but don't forget to
change file name in config.yml (File must be formatted in YAML style!)

$ character will be replaced with variable
You can format that variable (color,style) with {} brackets
e.g.:
{&a&l}$ ... green bold
{&e}$ ... yellow
{&f&l&o}$ ... white bold italic

You can specify variable order too!
{&6}$ {&d}$ ... default order (player name, message)
{&6;2}$ {&d;1}$ ... custom order (message, player name)

so you can do it with number after ; character
{&f;1}$ = first variable
{&f;2}$ = second variable
{&f;3}$ = third variable
...

note: {;1}$ will not work (there must be some format character)



If you want to insert symbols to config (e.g. ■) you have to replace it with unicode number

For example:
- Want to use ■ symbol
- Find it here: http://unicode-table.com/en/ or somewhere else
- Found this unicode number: "U+25A0" so the final form is "\u25a0"
