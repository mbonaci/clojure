_Made possible by Aphyr's excellent, from-the-ground-up Clojure tutorial and The joy of Clojure book_

# Basics

```clojure
user=> inc
<core$inc clojure.core$inc@d6206b5>
```

Valid Clojure expressions consist of _numbers_, _symbols_, _keywords_, _booleans_, _characters_, _functions_, _function calls_, _macros_, _strings_, _literal maps_, _vectors_, and _sets_. 
Numbers, strings and keywords evaluate to themselves. 

> single quote (and `quote` symbol) is used to include literal scalars in a program without evaluating them

Symbols (references to other values) start with a tick.

```clojure
user=> 'inc
inc

user=> (inc 2)
3

user=> (dec 2)
1

user=> '(1 (2 (3 ())))
(1 (2 (3 ())))

user=> '(inc 0)
(inc 0)

user=> (inc 0)
1

user=> (inc (inc 0))
2
```

## Numbers

```clojure
user=> (+ 1 (- 5 2) (+ 3 4))
11

user=> (type 3)
java.lang.Long

user=> Long/MAX_VALUE
9223372036854775807

user=> (inc (bigint Long/MAX_VALUE))
9223372036854775808N

user=> (type 5N)
clojure.lang.BigInt

user=> (type (int 0))
java.lang.Integer
;; Clojure's implementation of integer can theoretically take an infinitely 
;; large value, limited only by the available memory

user=> (type (short 0))
java.lang.Short

user=> (type (byte 0))
java.lang.Byte

;; decimal, hexadecimal, octal, radix-32, and binary literals:
[127 0x7F 0177 32r3V 2r01111111]
user=> [127 127 127 127 127]
;; radix supports up to base 36

user=> (Short/MAX_VALUE)
32767

user=> (Integer/MAX_VALUE)
2147483647

user=> Byte/MAX_VALUE
127

user=> (type 1.23)
java.lang.Double

user=> (type (float 1.23))
java.lang.Float

user=> 366e3
366000.0

user=> (type 1/3)
clojure.lang.Ratio

;; rational numbers are automatically simplified, if possible:
user=> 100/25
4

user=> (+ 1 2.0)
3.0

user=> (= 3 3.0)
false

user=> (== 3 3.0)
true

user=> (* 2 3 1/5)
6/5

user=> (- 5 1 1 1)
2

user=> (- 2)
-2

user=> (* 4)
4

user=> (/ 4)
¼

user=> (+)
0

user=> (*)
1

user=> (<= 1 2 3)
true

user=> (<= 1 3 2)
false

user=> (= 2 2 3)
false

user=> (= 2 2 2)
true
```

## Strings and Characters

Any sequence of characters enclosed in double quotes, including newlines:

```clojure
user=> "this is a string
  #_=> on two lines"
"this is a string\non two lines"  ;; when printed in REPL includes newline escape

user=> (type "a")
java.lang.String

user=> (str nil)
""
user=> (str 'a')
"a'"

user=> (str 'cat)
"cat"

user=> (str 1)
"1"

user=> (str '1)
"1"

user=> (str true)
"true"

user=> (str '(1 2 3))
"(1 2 3)"

user=> (str "meow " 3 " times")
"meow 3 times"

;; characters are denoted by backslash
user=> \a
\a

user=> \u0042
\B

user=> \\
\\
```

`#"..."` is Clojure’s way of writing a regular expression.
The parentheses mean that the regular expression should capture that part of the match. 
We get back a list containing the part of the string that matched the first parentheses, 
followed by the part that matched the second parentheses:

```clojure
user=> (re-find #"cat" "mystic cat mouse")
"cat"

user=> (re-find #"cat" "only dogs here")
nil

user=> (re-matches #"(.+):(.+)" "mouse:treat")
["mouse:treat" "mouse" "treat"]

user=> (rest (re-matches #"(.+):(.+)" "mouse:treat"))
("mouse" "treat")
```

## Booleans

**The only negative values** are `false` and `nil`, all others are `true`:

```clojure
user=> (boolean nil)
false
user=> (boolean 0)
true
user=> (boolean "hi")
true
user=> (boolean str)
true

user=> (and true false true)
false
user=> (and true true true)
true
```

... and returns the first _falsy_ value, or the last value if all are _truthy_:
```clojure
user=> (and 1 2 3)
3
user=> (and -1 nil 2)
nil
```

or returns the first _truthy_ value, or the last value if all are _falsy_:
```clojure
user=> (or false 2 3)
2
user=> (or false false nil)
nil
```

`not` inverses the _truthiness_ of the expression:
```clojure
user=> (not nil)
true
```

Symbols can have either short or full names. The short name is used to refer to things locally, and the _fully qualified name_ is used to refer unambiguously to a symbol from anywhere
Symbol names are separated with a `/`. For instance, the symbol `str` is also present in a family called `clojure.core`; the corresponding full name is `clojure.core/str`
The purpose of symbols is to refer to _things_, to point to other values. When evaluating a program, symbols are looked up and replaced by their corresponding values. 
That’s not the only use of symbols, but it’s the most common

```clojure
user=> (= str clojure.core/str)
true

user=> (name 'clojure.core/str)
"str"
```


# Keywords

Closely related to symbols and strings are keywords, which begin with a `:`. Keywords are like strings in that they’re made up of text, but are specifically intended for use as labels or identifiers. These aren’t labels in the sense of symbols, keywords aren’t replaced by any other value. They’re just names, by themselves.

```clojure
user=> (type :cat)
clojure.lang.Keyword

user=> (str :cat)
":cat"

user=> (name :cat)
"cat"
```

# Collections

A collection is a group of values. It’s a container which provides some structure, some framework, for the things that it holds. We say that a collection contains elements, or members.

> when a collection is evaluated, each of its contained items is evaluated first

## Lists

Literal lists are written with parentheses: `(yankee hotel foxtrot)`.

When a list is evaluated, the first item of the list, `yankee`, will be resolved to a _function_, _macro_, or _special form_. 
If `yankee` is a _function_, the remaining items in the list will be evaluated in order, and the results will be passed to `yankee` as its parameters.
If `yankee` is a _macro_ or a _special form_, the remaining items in the list
aren’t necessarily evaluated, but are processed as defined by the _macro_ or _operator_.

> **special form** is a form with special syntax or special evaluation rules that are typically not implemented using the base Clojure forms. An example of a special form is
the `.` (dot) operator used for Java interoperability purposes.

```clojure
user=> (cons 1 (2 3))

ClassCastException java.lang.Long cannot be cast to clojure.lang.IFn
;; which basically means that the number 2 cannot be used as a function

user=> (cons 1 '(2 3))
(1 2 3)
```

Remember, we quote lists (any everything else) with a `'` (or `quote`) to prevent them from being evaluated.  

```clojure
user=> '(1 2 3)
(1 2 3)

user=> (type '(1 2 3))
clojure.lang.PersistentList
```

There's also a **syntax-quote**, which automatically qualifies all unqualified symbols in its argument:

```clojure

```

You can also construct a list using list:

```clojure
user=> (list 1 2 3)
(1 2 3)
user=> (= (list 1 2) (list 1 2))
true
```

You can modify a list by _conjoining_ an element onto it (notice it goes to the beginning of the list):

```clojure
user=> (conj (list 1 2 3) 4)
(4 1 2 3)

user=> (first (list 1 2 3))
1
user=> (second (list 1 2 3))
2
user=> (nth (list 1 2 3) 2)
3
```

Unlike some _Lisps_, the empty list in Clojure, `()`, isn't the same as `nil`.
Lists are well-suited for small collections, or collections which are read in linear order, but are slow when you want to get arbitrary elements from later in the list. 


# Vectors

For fast access to every element, we use a __vector__.
Vectors are surrounded by square brackets, just like lists are surrounded by parentheses. Because vectors aren’t evaluated like lists are, there’s no need to quote the vector literal:

```clojure
user=> [1 2 3]
[1 2 3]

user=> (type [1 2 3])
clojure.lang.PersistentVector
```

You can also create vectors with vector, or change other structures into vectors with vec:

```clojure
user=> (vector 1 2 3)
[1 2 3]

user=> (vec (list 1 2 3))
[1 2 3]
```

`conj` on a vector adds to the end, not the start:

```clojure
user=> (conj [1 2 3] 4)
[1 2 3 4]
```

Our friends `first`, `second`, and `nth` work here too; but unlike lists, `nth` is fast on vectors. That’s because internally, vectors are represented as a very broad tree of elements, where each part of the tree branches into 32 smaller trees. Even very large vectors are only a few layers deep, which means getting to elements only takes a few hops.

The important difference, when compared to lists, is that vectors evaluate each item in order. No function or macro is performed on a vector itself.

`rest` and `next` both return _everything but the first element_. They differ only by what happens when there are no remaining elements:

```clojure
user=> (rest [1 2 3 4])
(2 3 4)

user=> (next [1 2 3 4])
(2 3 4)
```

`rest` returns logical `true`, `next` returns logical `false`. Each has their uses, but in almost every case they’re equivalent:

```clojure
user=> (next [1])
nil

user=> (rest [1])
()
```

`last`, surprisingly, returns the last element:

```clojure
user=> (last [1 2 3])
3
```

`count`, shockingly, returns element count:

```clojure
user=> (count [1 2 3 4])
4
```

You can use _index_ to access vector elements:

```clojure
user=> ([1 2 3] 1)
2
```

Vectors and lists containing the same elements are considered equal:

```clojure
user=> (= [1 2] (list 1 2))
true
```

In almost all contexts, you can consider vectors, lists, and other sequences as interchangeable. They only differ in their performance characteristics, and in a few data-structure-specific operations.


# Sets

Sets are surrounded by `#{...}`. In general, the order of sets can shift at any time. If you want a particular order, you can ask for it as a list or a vector:

```clojure
user=> #{1 2 3}
#{1 2 3}
```

To ask for elements in a sorted order:

```clojure
user=> (sort #{2 4 1})
(1 2 4)
```

`conj` adds an element to the set:

```clojure
user=> (conj #{:a :b :c} :d)
#{:a :c :b :d}
```

`disj` removes an element:

```clojure
user=> (disj #{1 2} 2)
#{1}
```

`contains?` checks for existence of an element:

```clojure
user=> (contains? #{1 2 3} 3)
true
```

Like vectors, you can use the set itself as a verb. Unlike `contains?`, this expression returns the element itself (if it was present), or `nil`:

```clojure
user=> (#{1 2 3} 3)
3
user=> (#{1 2 3} 4)
nil
```

You can make a set out of any other collection with `set`:

```clojure
user=> (set [2 5 1])
#{1 2 5}
```


# Maps

Maps are surrounded by braces `{...}`, filled by alternating keys and values. In this map, the three keys are `:name`, `:color`, and `:weight`, and their values are `"mittens"`, `"black"`, and `9`, respectively. We can look up the corresponding value for a key with `get`:

```clojure
user=> {:name "luka", :weight 3, :color "white"}
{:weight 3, :name "luka", :color "white"}

user=> (get {"cat" "meow", "dog" "woof"} "cat")
"meow"
```

`get` can also take a default value to return instead of `nil`, if the key doesn’t exist in that map:

```clojure
user=> (get {:glinda :god} :wicked :not-here)
:not-here
```

We can use maps as verbs, directly:

```clojure
user=> ({"a" 12, "b" 24} "b")
24
```

Keywords can also be used as verbs, which look themselves up:

```clojure
user=> (:raccoon {:weasel "queen", :raccoon "king"})
"king"
```

`assoc` adds an element to a map: 

```clojure
user=> (assoc {:bolts 1088} :camshafts 3)
{:camshafts 3, :bolts 1088}
```

`assoc` adds keys if they aren’t present, and replaces values if they’re already there. If you associate a value onto `nil`, it creates a new map

```clojure
user=> (assoc nil 5 2)
{5 2}
```

Combine maps together using `merge`. It yields a map containing all the elements of all given maps, preferring the values from later ones:

```clojure
user=> (merge {:a 1, :b 2} {:b 3, :c 4})
{:c 4, :a 1, :b 3}
```

Remove map element with `dissoc`:

```clojure
user=> (dissoc {:a 1, :b 2, :c 4} :c)
{:a 1, :b 2}
```

Like vectors, any item in a map literal is evaluated before the result is stored in the map. Unlike vectors, the order in which they are evaluated isn't guaranteed.

# Symbols

Typically used to refer to function parameters, local variables, globals, and Java classes.

We can define a meaning for a symbol within a specific expression, using `let`.
The `let` expression first **takes a vector of bindings**: alternating symbols and values that those symbols are bound to, within the remainder of the expression. 
“Let the symbol `cats` be `5`, and construct a string composed of `"I have "`, `cats`, and `" cats"`:

```clojure
user=> (let [cats 5] (str "I have " cats " cats."))
"I have 5 cats."
```

Let bindings, also called **locals**, apply only within the `let` expression itself. They also override any existing definitions for symbols at that point in the program. For example, we can redefine addition to mean subtraction, for the duration of a `let`:

```clojure
user=> (let [+ -] (+ 2 3))
-1
```

That definition doesn’t apply outside the `let`:

```clojure
user=> (+ 2 3)
5
```

We can also provide multiple bindings. Since Clojure doesn’t care about spacing, alignment, or newlines, we’ll write this on multiple lines, for clarity.

```clojure
user=> (let [person "joseph"
  #_=>       num-cats 186]
  #_=>   (str person " has " num-cats " cats!"))  ; the body
"joseph has 186 cats!"
```

When multiple bindings are given, they are evaluated in order. Later bindings can use previous bindings.

```clojure
user=> (let [cats 3
  #_=>       legs (* 4 cats)]
  #_=> (str legs " legs all together"))
"12 legs all together"
```

> a symbol wholse name is prefixed with a namespace, followed by a slash, is called **qualified symbol**:

```clojure
user=> clojure.core/map
#<core$map clojure.core$map@2a0406c4>

user=> clojure.set/union
#<set$union clojure.set$union@1be2bcc8>
```

The body is sometimes described as an _implicit do_ (see [blocks bellow](#blocks)) because it follows the same rules: you may include any number of expressions and all will be evaluated, but only the value of the last one is returned.

Because they’re immutable, _locals_ can’t be used to accumulate results. Instead,
you’d use a high level function or loop/recur form.

To summarize, `let` defines the meaning of symbols within an expression. When Clojure evaluates a `let`, it replaces all occurrences of those symbols in the rest of the `let` expression with their corresponding values, then evaluates the rest of the expression.

# Functions

Functions are a first-class type in Clojure. They can be used the same as any value (stored in Vars, held in collections and passed as arguments and returned as a result of other functions).

> prefix notation allows any number of arguments (infix only two) and completely eliminates the problem of operator precedence.

```clojure
let( [x] (+ x 1))
```

We can’t actually evaluate this program, because there’s no value for `x` yet. It could be `1`, or `4`, or `1453`. We say that `x` is _unbound_, because it has no binding to a particular value. This is the nature of the __function__: an expression with unbound symbols.

Function definition:

```clojure
user=> (fn [x] (+ x 1))
#<user$eval1487$fn__1488 user$eval1487$fn__1488@6b7d28db>
```

Named function definition:

```clojure
user=> (let [twice (fn [x] (* 2 x))]
  #_=>   (+ (twice 1)
  #_=>   (twice 3)))
8
```

`let` bindings describe a similar relationship, but with a specific set of values for those arguments. `let` is evaluated immediately, whereas `fn` is evaluated later, when bindings are provided.

# Vars

Once a `let` is defined, there’s no way to change it. If we want to redefine symbols for everyone, even code that we didn’t write, we need a new construct, a mutable variable.

```clojure
user=> (def cats 5)
#'user/cats

user=> (type #'user/cats)
clojure.lang.Var

user=> cats
5
```

`def` defines a type of value we haven’t seen before: a _Var_. _Vars_, like symbols, are references to other values. When evaluated, a `Var` is replaced by its corresponding value.  

`def` also binds the symbol `cats` (and its globally qualified equivalent `user/cats`) to that `Var`.  

The symbol `inc` points to the `Var` `#'inc`, which in turn points to the function `#<core$inc clojure.core$inc@16bc0b3c>`. We can see the intermediate Var with `resolve`:

```clojure
user=> 'inc     ;symbol
inc

user=> (resolve 'inc)
#'clojure.core/inc    ;variable

user=> (eval 'inc)
#<core$inc clojure.core$inc@d6206b5>  ;value
```

Why those two levels of indirection? Unlike with symbol, we can change the meaning of a Var for everyone, globally, at any time.  

Vars don't require a value. Instead we can just declare them and, by doing so, defer the binding of value.

```clojure
user=> (def y)
#'user/y

;; if we try to use it:
user=> y
java.lang.IllegalStateException: Var user/y is unbound
```

## Named functions

```clojure
user=> (def half 
user=>   (fn [number] (/ number 2)))
#'user/half

user=> (half 8)
4
```

Creating a function and binding it to a variable is so common that it has its own form: `defn`, which is a _macro_ that is short for `def fn`:

```clojure
user=> (defn half [number] (/ number 2))
#'user/half

user=> (half 8)
4
```

Functions don’t have to take an argument. We’ve seen functions which take zero arguments, like `(+)`:

```clojure
user=> (defn half [] 1/2)
#'user/half
```

But if we try to use our earlier form with one argument, Clojure complains that the *arity* (the number of arguments to a function) is incorrect:

```clojure
user=> (half 8)

ArityException Wrong number of args (1) passed to: user$half  clojure.lang.AFn.throwArity (AFn.java:437)
```

To handle multiple arities, functions have an alternate form, instead of an argument vector and a body, one provides a series of lists, each of which starts with an argument vector, followed by the body:

```clojure
user=> (defn half
  #_=>   ([] 1/2)
  #_=>   ([x] (/ x 2)))
#'user/half

user=> (half)
1/2

user=> (half 8)
4
```

Multiple arguments work just like you expect. Just specify an argument vector of two, or three, or however many arguments the function takes.  
Some functions can take any number of arguments. For that, Clojure provides `&`, which slurps up all remaining arguments as a list:

```clojure
user=> (defn vargs [x y & more-args]
  #_=>   {:x x
  #_=>    :y y
  #_=>    :more more-args})
#'user/vargs

user=> (vargs 1 2)
{:x 1, :y 2, :more nil}

user=> (vargs 1 2 3 4 5)
{:x 1, :y 2, :more (3 4 5)}
```

`x` and `y` are mandatory, though there don’t have to be any remaining arguments.  

To keep track of what arguments a function takes, why the function exists, and what it does, we usually include a **docstring**. Docstrings help fill in the missing context around functions, to explain their assumptions, context, and purpose to the world:

```clojure
user=> (defn launch
  #_=>   "Launches a spacecraft into the given orbit by initiating a
  #_=>    controlled on-axis burn. Does not automatically stage, but
  #_=>    does vector thrust, if the craft supports it."
  #_=>   [craft target-orbit]
  #_=>   "OK, we don't know how to control spacecraft yet.")
#'user/launch
```

Docstrings are used to automatically generate documentation for a Clojure programs, but you can also access them from the **REPL** (The `user=>` prompt refers to the top-level namespace of the default REPL):

```clojure
user=> (doc launch)
-------------------------
user/launch
([craft target-orbit])
  Launches a spacecraft into the given orbit by initiating a
   controlled on-axis burn. Does not automatically stage, but
   does vector thrust, if the craft supports it.
nil
```

`doc` tells us the full name of the function, the arguments it accepts, and its docstring. This information comes from the `launch` var’s metadata, and is saved there by `defn`. We can inspect metadata directly with the `meta` function:

```clojure
user=> (meta #'launch)
{:arglists ([craft target-orbit]), :ns #<Namespace user>, :name launch, :column 1, :doc "Launches a spacecraft into the given orbit by initiating a\n   controlled on-axis burn. Does not automatically stage, but\n   does vector thrust, if the craft supports it.", :line 1, :file "/tmp/form-init523009510157887861.clj"}
```

There’s some other juicy information in there, like the file the function was defined in and which line and column it started at, but that’s not particularly useful since we’re in the _REPL_, not a file. However, this does hint at a way to answer our motivating question: how does the type function work?

## Blocks

When you have a series or block of expressions that need to be treated as one, use `do`. All the expressions will be evaluated, but only the last one will be returned:

```clojure
user=> (do
  #_=>   6
  #_=>   (+ 5 4)
  #_=>   3)
3
```

The expressions `6` and `(+ 5 4)` are perfectly legal. The addition in `(+ 5 4)` is even done, but the value is thrown away, only the final expression `3` is returned

## How does `type` work?

`type`, like all functions, is a kind of object with its own unique type:

```clojure
user=> type
#<core$type clojure.core$type@2761df2a>

user=> (type type)
clojure.core$type
```

This tells us that `type` is a particular instance, at memory address `39bda9b9`, of the type `clojure.core$type`. 
`clojure.core` is a namespace which defines the fundamentals of the Clojure language, and `$type` tells us that it’s named type in that namespace. None of this is particularly helpful, though. Maybe we can find out more about the `clojure.core$type` by asking what its supertypes are:

```clojure
user=> (supers (type type))
#{java.io.Serializable java.lang.Runnable clojure.lang.AFunction clojure.lang.IMeta clojure.lang.AFn java.lang.Object clojure.lang.IObj java.util.Comparator clojure.lang.Fn java.util.concurrent.Callable clojure.lang.IFn}
```

This is a set of all the types that include `type`. We say that `type` is an instance of `clojure.lang.AFunction`, or that it implements or extends `java.util.concurrent.Callable`, and so on. Since it’s a member of `clojure.lang.IMeta` it has metadata, and since it’s a member of `clojure.lang.AFn`, it’s a function. Just to double check, let’s confirm that `type` is indeed a function:

```clojure
user=> (fn? type)
true
```

`type` can take a single argument, which it calls `x`. If it has `:type` metadata, that’s what it returns. Otherwise, it returns the class of `x`. Let’s take a deeper look at type’s metadata for more clues:

```clojure
user=> (doc type)
-------------------------
clojure.core/type
([x])
  Returns the :type metadata of x, or its Class if none
nil
```

This function was first added to Clojure in version `1.0`, and is defined in the file `clojure/core.clj`, on line `3109`:

```clojure
user=> (meta #'type)
{:ns #<Namespace clojure.core>, :name type, :arglists ([x]), :column 1, :added "1.0", :static true, :doc "Returns the :type metadata of x, or its Class if none", :line 3109, :file "clojure/core.clj"}
```

We could go dig up the Clojure source code and read its definition there, or we could ask Clojure to do it for us. Aha! Here, at last, is how `type` works. It’s a function which takes a single argument `x`, and returns either `:type` from its metadata, or `(class x)`.

```clojure
user=> (source type)
(defn type 
  "Returns the :type metadata of x, or its Class if none"
  {:added "1.0"
   :static true}
  [x]
  (or (get (meta x) :type) (class x)))
nil
```


# Sequences
## Recursion

`cons`, makes a list beginning with the first argument, followed by all the elements in the second argument:

```clojure
user=> (cons 1 [2 3 4])
(1 2 3 4)
```

Problem of incrementing all elements of a vector:

```clojure
user=> (defn inc-first [nums]
  #_=>   (if (first nums)
  #_=>     ; If there's a first num, build a new list with cons
  #_=>     (cons (inc (first nums))
  #_=>           (rest nums))
  #_=>     ; If there's no first num, return an empty list
  #_=>     (list)))
#'user/inc-first

user=> (inc-first [])
()

user=> (inc-first [1 2 3])
(2 2 3)
```

What if we called our function on `rest`?

```clojure
user=> (defn inc-all [nums]
  #_=>   (if (first nums)
  #_=>     (cons (inc (first nums))
  #_=>           (inc-all (rest nums)))
  #_=>     (list)))
#'user/inc-all

user=> (inc-all [1 2 3 4])
(2 3 4 5)
```

This technique is called _recursion_, and it is a fundamental principle in working with collections, sequences, trees, graphs or any problem which has small parts linked together. There are two key elements in a recursive program:
 - Some part of the problem which has a known solution
 - A relationship which connects one part of the problem to the next

Incrementing the elements of an empty list returns the empty list. This is our **base case**, the ground to build on. Our **inductive case**, also called the _recurrence relation_, is how we broke the problem up into incrementing the first number in the sequence, and incrementing all the numbers in the rest of the sequence. The `if` expression bound these two cases together into a single function, a function defined in terms of itself.
Let’s parameterize our `inc-all` function to use any transformation of its elements:

```clojure
user=> (defn transform-all [f xs]
  #_=>   (if (first xs)
  #_=>     (cons (f (first xs))
  #_=>           (transform-all f (rest xs)))
  #_=>     (list)))
#'user/transform-all

user=> (transform-all inc [1 2 3 4])
(2 3 4 5)
```

## Loop

When using recursion, you sometimes want to loop back not to the top of the function, but to somewhere inside the function body.  
The `loop` acts exactly like `let` but provides a target for `recur` to jump to:

```clojure
user=> (defn sum-down-from [initial-x]
  #_=>   (loop [sum 0, x initial-x]
  #_=>     (if (pos? x)
  #_=>       (recur (+ sum x) (dec x))
  #_=>       sum)))
#'user/sum-down-from
```

Upon entering the `loop`, the locals `sum` and `x` are initialized (like in `let`). A `recur` always loops back to the closest enclosing `loop` or `fn`. The `loop` locals are rebound to the values given in `recur`.  
`recur` works only from the tail position.  

`keyword` transforms a string to keyword:

```clojure
user=> (transform-all keyword ["aa" "bb" "cc"])
(:aa :bb :cc)
```

To wrap every element in a list:

```clojure
user=> (transform-all list ["aa" "bb" "cc"])
(("aa") ("bb") ("cc"))
```

We basically implemented `map` function:

```clojure
user=> (map inc [1 2 3 4])
(2 3 4 5)
```

The function `map` relates one sequence to another. The type `map` relates keys to values. There is a deep symmetry between the two: maps are usually sparse, and the relationships between keys and values may be arbitrarily complex. The `map` function, on the other hand, usually expresses the same type of relationship, applied to a series of elements in a fixed order.

Clojure has a _special form_ called `recur` that's specifically used for tail recursion:

```clojure
user=> (defn print-down-from [x]
  #_=>   (when (pos? x)  ;return when x is no longer positive
  #_=>     (println x)
  #_=>     (recur (dec x))))
#'user/print-down-from

user=> (print-down-from 5)
5
4
3
2
1
nil
```

> `when` is same as `if`, except it doesn't have the `else` part and it requires an implicit `do` in order to perform side-effects

This is nearly identical to how you’d structure a while loop in an imperative language.One significant difference is that the value of `x` isn’t decremented somewhere in the body of the loop. Instead, a new value is calculated as a parameter to `recur`, which immediately does two things: rebinds `x` to the new value and returns control to the top of `print-down-from`.  
If the function has multiple arguments, the `recur` call must as well, just as if you were calling the function by name instead of using the `recur` special form. And just as with a function call, the expressions in the `recur` are evaluated in order first and only then bound to the function arguments simultaneously.

## Building Sequences

We can use recursion to expand a single value into a sequence of values, each related by some function. For instance (`pos?` returns `true` if `num` is greater than zero, else `false`):

```clojure
user=> (defn expand [f x count]
  #_=>   (if (pos? count)
  #_=>     (cons x (expand f (f x) (dec count)))))
#'user/expand

user=> (expand inc 0 10)
(0 1 2 3 4 5 6 7 8 9)
```

Our base case is `x` itself, followed by the sequence beginning with `(f x)`. That sequence in turn expands to `(f (f x))`, and then to `(f (f (f x)))`, and so on. Each time we call `expand`, we count down by one using `dec`. Once the `count` is zero, `if` returns `nil`, and evaluation stops.

Clojure has a more general form of this function, called `iterate`:

```clojure
user=> (take 10 (iterate inc 0))
(0 1 2 3 4 5 6 7 8 9)
```

Since this sequence is infinitely long, we’re using `take` to select only the first 10 elements. We can construct more complex sequences by using more complex functions:

```clojure
user=> (take 10 (iterate (fn [x] (if (odd? x) (+ 1 x) (/ x 2))) 10))
(10 5 6 3 4 2 1 2 1 2)
```

`repeat` constructs a sequence where every element is the same:

```clojure
user=> (take 10 (repeat "a"))
("a" "a" "a" "a" "a" "a" "a" "a" "a" "a")

user=> (repeat 5 "b")
("b" "b" "b" "b" "b")
```

`repeatedly` simply calls a function `(f)` to generate an infinite sequence of values, over and over again, without any relationship between elements. For an infinite sequence of random numbers:

```clojure
user=> (rand)
0.6934524557647231

user=> (rand)
0.1355414232605504

user=> (take 3 (repeatedly rand))
(0.18806021884865332 0.5231673860825672 0.38244349544358525)
```

`range` generates a sequence of numbers between two points. 
`(range n)` gives `n` successive integers starting at 0. 
`(range n m)` returns integers from `n` to `m-1`. 
`(range n m step)` returns integers from `n` to `m`, separated by `step`:

```clojure
user=> (range 5)
(0 1 2 3 4)

user=> (range 5 8)
(5 6 7)

user=> (range 5 25 5)
(5 10 15 20)
```

To extend a sequence by repeating it forever, use `cycle`:

```clojure
user=> (take 6 (cycle (range 5 50 5)))
(5 10 15 20 25 30)
```


## Transforming Sequences

`map` applies a function to each element, but it has a few more tricks up its sleeve:

```clojure
user=> (map (fn [n vehicle] (str "I've got " n " " vehicle "s"))
  #_=>   [0 200 9]
  #_=>   ["car" "train" "kiteboard"])
("I've got 0 cars" "I've got 200 trains" "I've got 9 kiteboards")
```

If given multiple sequences, `map` calls its function with one element from each sequence in turn. So the first value will be `(f 0 "car")`, the second `(f 200 "train")`, and so on. Like a zipper, map folds together corresponding elements from multiple collections. To sum three vectors, column-wise:

```clojure
user=> (map + [1 2 3]
  #_=>        [4 5 6]
  #_=>        [3 2 1])
(8 9 10)
```

If one sequence is bigger than another, `map` stops at the end of the smaller one. We can exploit this to combine finite and infinite sequences. For example, to number the elements in a vector:

```clojure
user=> (map (fn [index element] (str index ". " element))
  #_=>      (iterate inc 0)
  #_=>      ["erlang" "scala" "haskell"])
("0. erlang" "1. scala" "2. haskell")
```

Transforming elements together with their indices is so common that Clojure has a special function for it: `map-indexed`:

```clojure
user=> (map-indexed (fn [index element] (str index ". " element))
  #_=>              ["erlang" "scala" "haskell"])
("0. erlang" "1. scala" "2. haskell")
```

You can also tack one sequence onto the end of another, like so:

```clojure
user=> (concat [1 2 3] [:a :b :c] [4 5 6])
(1 2 3 :a :b :c 4 5 6)
```

Another way to combine two sequences is to riffle them together, using `interleave`:

```clojure
user=> (interleave [:a :b :c] [1 2 3])
(:a 1 :b 2 :c 3)
```

And if you want to insert a specific element between each successive pair in a sequence, try `interpose`:

```clojure
user=> (interpose :and [1 2 3 4])
(1 :and 2 :and 3 :and 4)
```

To reverse a sequence, use ... you guessed it, `reverse`:

```clojure
user=> (reverse [1 2 3])
(3 2 1)

user=> (reverse "woolf")
(\f \l \o \o \w)
```

Strings are sequences too! Each element of a string is a character, written `\f`. You can rejoin those characters into a string with `apply str`:


```clojure
user=> (apply str (reverse "woolf"))
"floow"
```

…and break strings up into sequences of chars with `seq`:

```clojure
user=> (seq "sato")
(\s \a \t \o)
```

To randomize the order of a sequence, use `shuffle`:

```clojure
user=> (shuffle [1 2 3 4 5])
[4 3 5 1 2]

user=> (apply str (shuffle (seq "abracadabra")))
"raradbabaac"
```


# Subsequences

`take` selects the first `n` elements
`drop` removes the first `n` elements
`take-last` and `drop-last` operate on the last `n` elements:

```clojure
user=> (take 3 (range 10))
(0 1 2)

user=> (drop 3 (range 10))
(3 4 5 6 7 8 9)

user=> (take-last 3 (range 10))
(7 8 9)

user=> (drop-last 3 (range 10))
(0 1 2 3 4 5 6)
```

`take-while` and `drop-while` work just like `take` and `drop`, but use a function to decide when to stop:

```clojure
user=> (take-while pos? [3 2 1 0 -1 -2 10])
(3 2 1)
```

In general, one can cut a sequence in twain by using `split-at` with a particular index. There’s also `split-with`, which uses a function to decide when to cut:

```clojure
user=> (split-at 4 (range 10))
[(0 1 2 3) (4 5 6 7 8 9)]

user=> (split-with number? [1 2 3 :mark 4 5 6 :mark 7])
[(1 2 3) (:mark 4 5 6 :mark 7)]
```

Notice that because indexes start at zero, sequence functions tend to have predictable number of elements.`(split-at 4)` yields four elements in the first collection, and ensures the second collection begins at index four. `(range 10)` has ten elements, corresponding to the first ten indices in a sequence. `(range 3 5)` has two (as in `5 - 3 = 2`) elements. These choices simplify the definition of recursive functions as well.
We can select particular elements from a sequence by applying a function. To pull up all positive numbers in a list, use `filter`:

```clojure
user=> (filter pos? [1 5 -4 -7 3 0])
(1 5 3)
```

`filter` looks at each element in turn, and includes it in the resulting sequence only if `(f element)` returns a _truthy_ value. Its complement is `remove`, which only includes those elements where `(f element)` is `false` or `nil`:

```clojure
user=> (remove string? [1 "tur" :apple])
(1 :apple)
```

One can group a sequence into chunks using `partition`, `partition-all`, or `partition-by`. For instance, one might group alternating values into pairs like this:

```clojure
user=> (partition 2 [:cats 5 :bats 27 :crocs 0])
((:cats 5) (:bats 27) (:crocs 0))
```

Separate a series of numbers into negative and positive subsequences:

```clojure
user=> (partition-by neg? [1 2 3 2 1 -1 -2 -3 -2 -1 1 2])
((1 2 3 2 1) (-1 -2 -3 -2 -1) (1 2))
```

`partition-all` may include partitions with fewer than `n` items at the end:

```clojure
user=> (partition-all 3 [1 2 -5 3 2 1 -1 -2 -3 -2 -1 1 2])
((1 2 -5) (3 2 1) (-1 -2 -3) ( -2 -1 1) ( 2))
```

while `partition` may not:

```clojure
user=> (partition 3 [1 2 -5 3 2 1 -1 -2 -3 -2 -1 1 2])
((1 2 -5) (3 2 1) (-1 -2 -3) (-2 -1 1))
```


## Collapsing subsequences

After transforming a sequence, we often want to collapse it in some way, in order to derive some smaller value. For instance, we might want the number of times each element appears in a sequence:

```clojure
user=> (frequencies [:meow :mrrrow :meow :meow])
{:meow 3, :mrrrow 1}
```

To group elements by some function:

```clojure
user=> (pprint (group-by :first [{:first "Li"    :last "Zhou"}
  #_=>                           {:first "Sarah" :last "Lee"}
  #_=>                           {:first "Sarah" :last "Dunn"}
  #_=>                           {:first "Li"    :last "O'Toole"}]))
{"Li" [{:last "Zhou", :first "Li"} {:last "O'Toole", :first "Li"}],
 "Sarah" [{:last "Lee", :first "Sarah"} {:last "Dunn", :first "Sarah"}]}
```

Here we’ve taken a sequence of people with first and last names, and used the `:first` keyword (which can act as a function!) to look up those first names. `group-by` used that function to produce a map of first names to lists of people, kind of like an index.
In general, we want to combine elements together in some way, using a function. Where `map` treated each element independently, reducing a sequence requires that we bring some information along. The most general way to collapse a sequence is `reduce`:

```clojure
user=> (doc reduce)
-------------------------
clojure.core/reduce
([f coll] [f val coll])
  f should be a function of 2 arguments. If val is not supplied,
  returns the result of applying f to the first 2 items in coll, then
  applying f to that result and the 3rd item, etc. If coll contains no
  items, f must accept no arguments as well, and reduce returns the
  result of calling f with no arguments.  If coll has only 1 item, it
  is returned and f is not called.  If val is supplied, returns the
  result of applying f to val and the first item in coll, then
  applying f to that result and the 2nd item, etc. If coll contains no
  items, returns val and f is not called.

user=> (reduce + [1 2 3 4])
10

user=> (reduce + 1 [1 2 3 4])
11
```

To see the reducing process in action, we can use `reductions`, which returns a sequence of all the intermediate states:

```clojure
user=> (reductions + [1 2 3 4])
(1 3 6 10)
```

Oftentimes we include a default state to start with. For instance, we could start with an empty set, and add each element to it as we go along:

```clojure
user=> (reduce conj #{} [:a :b :b :b :a :c])
#{:a :c :b}
```

Reducing elements into a collection has its own name: `into`. We can `conj [key value]` vectors into a map, for instance, or build up a list:

```clojure
user=> (into {} [[:a 2] [:b 3]])
{:a 2, :b 3}

user=> (into (list) [1 2 3 4])
(4 3 2 1)
```

Because **elements added to a list appear at the beginning**, not the end, this expression **reverses the sequence**. Vectors `conj` onto the end, so to emit the elements in order, using reduce, we might try:

```clojure
user=> (reduce conj [] [1 2 3 4 5])
[1 2 3 4 5]
```

Remember?

```clojure
user=> (conj [-1 0] [1 2 3 4 5])
[-1 0 [1 2 3 4 5]]               ; not [-1 0 1 2 3 4 5]
```

This looks like a `map` function. All that’s missing is some kind of transformation applied to each element:

```clojure
user=> (defn my-map [f coll]
  #_=>   (reduce (fn [output element]
  #_=>             (conj output (f element)))
  #_=>           []
  #_=>           coll))
#'user/my-map

user=> (my-map inc [1 2 3 4])
[2 3 4 5]
```

So `map` is just a special kind of `reduce`. What about, say, `take-while`?

```clojure
user=> (defn my-take-while [f coll]
  #_=>   (reduce (fn [out elem]
  #_=>             (if (f elem)
  #_=>               (conj out elem)
  #_=>               (reduced out)))
  #_=>           []
  #_=>           coll))
```

We’re using a special function here, `reduced`, to indicate that we’ve completed our reduction early and can skip the rest of the sequence.

```clojure
user=> (my-take-while pos? [2 1 0 -1 0 1 2])
[2 1]
```

Most of Clojure’s **sequence functions are lazy**. For instance, we can increment every number from zero to infinity:

```clojure
user=> (def infseq (map inc (iterate inc 0)))
#'user/infseq

user=> (realized? infseq)
false
```

That function returned immediately. Because it hasn’t done any work yet, we say the sequence is unrealized. It doesn’t increment any numbers at all until we ask for them:

```clojure
user=> (take 10 infseq)
(1 2 3 4 5 6 7 8 9 10)

user=> (realized? infseq)
true
```

**Lazy sequences also remember their contents, once evaluated, for faster access.**

Find the sum of the products of consecutive pairs of the first 1000 odd integers.

```clojure
user=> (reduce +
  #_=>         (take 1000
  #_=>               (map (fn [pair] (* (first pair) (second pair)))
  #_=>                    (partition 2 1
  #_=>                      (filter odd?
  #_=>                        (iterate inc 0))))))
1335333000
```

__Homework:__

1. Write a function to find out if a string is a palindrome.

```clojure
user=> (defn palindrome? [word] (== 0 (compare word (apply str (reverse word)))))
```

2. Find the number of `c`s in `“abracadabra”`.

```clojure
user=> (defn occurs-count [c word]
  #_=>         (get (frequencies (seq word)) c 0))
```

3. Write your own version of `filter`.

```clojure

```







