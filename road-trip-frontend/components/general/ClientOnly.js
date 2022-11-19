import { useEffect, useState } from "react";

/**
 * Wrap this around components that should only render on the client.
 * Anything that uses atomsWithStorage MUST use this.
 * If you get hydration errors, try wrapping the component in this.
 * https://www.joshwcomeau.com/react/the-perils-of-rehydration/#abstractions
 *
 * @param {Object} props
 * @param {JSX.Element} props.children Items to render
 * @returns
 */
const ClientOnly = ({ children, ...props }) => {
    const [hasMounted, setHasMounted] = useState(false);

    useEffect(() => {
        setHasMounted(true);
    }, []);

    if (!hasMounted) {
        return null;
    }
    return <div {...props}>{children}</div>;
};

export default ClientOnly;
