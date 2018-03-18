# Programming Guide


## Tutorial
Coming eventually if I feel like it...
Notes
- One opcode per line, no exceptions.
- All opcodes that use values from the stack will remove them from the stack unless otherwise stated in the opcode description.

## Opcodes

### Special
| Opcode | Description |
| --- | --- |
| load \<literal\> | Loads a literal value (boolean/integer/float/string) onto the stack. The best compression method for this literal will be automatically selected by the compiler. |

### 1 Byte Opcodes
| Opcode | Description |
| --- | --- |
| pop | Removes a single value from the top of the stack. |
| pop 2 | Removes the top two values on the stack. |
| clr | Clears the stack. |
| swp | Swaps the top two values on the stack. |
| ldr a | Loads the value in register A onto the stack. |
| ldr b | Loads the value in register B onto the stack. |
| ldr c | Loads the value in register C onto the stack. |
| ldr d | Loads the value in register D onto the stack. |
| ldr i | Loads current iteration index in an iteration loop. Undefined behavior if used outside an iteration. |
| ldr o | Loads current object in an iteration loop. Undefined behavior if used outside an iteration.  |
| sdr a | Saves the top stack value into register A. |
| sdr b | Saves the top stack value into register B. |
| sdr c | Saves the top stack value into register C. |
| sdr d | Saves the top stack value into register D. |
| if = | Compares the top two values on the stack. Jumps to the next `endif` opcode not associated with a nested conditional if the two values are not equal. |
| if != | Compares the top two values on the stack. Jumps to the next `endif` opcode if the two values are equal. |
| if > | Compares the top two values on the stack. Jumps to the next `endif` opcode if the top value is greater than the next value. |
| if >= | Compares the top two values on the stack. Jumps to the next `endif` opcode if the top value is greater than or equal to the next value. |
| if < | Compares the top two values on the stack. Jumps to the next `endif` opcode if the top value is less than the next value. |
| if <= | Compares the top two values on the stack. Jumps to the next `endif` opcode if the top value is less than or equal to the next value. |
| else | Jumps to the next `endif` statement if the previous conditional passed. Implies an `endif` statement itself. |
| endif | This is a no-op. It serves as a marker for conditional blocks only. |
| iterate | Iterates over the top value on the stack. An `end` statement must be located later in the program. |
| iterate stack | Iterates over the top value on the stack and loads the iterated object onto the stack before each pass. An `end` statement must be located later in the program. |
| end | Marks the end of a loop from the `iterate` opcode. Jumps to the line after the associated `iterate` code if nessesary.
| print | Prints the top value on the stack to the output. |
| println | Prints the top value on the stack to the output, followed by a newline. |
| exit | Terminates the program and prints the top value on the stack to the output |
| terminate | Terminates the program instantly. |
| restart | Jumps to the first line of the program. |
| jump | Jumps to the line provided by the top value on the stack. |
| + | Adds the top two values on the stack. |
| - | Subtracts the top two values on the stack. |
| * | Multiplies the top two values on the stack. |
| / | Divides the top two values on the stack. |
| % | Performs the modulus operation on the top two values on the stack. |

### 2 Byte Opcodes
I'll write these at some point. There are a lot and more being added all the time! D:
